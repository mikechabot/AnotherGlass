package vino.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import vino.Controller;
import vino.Params;
import vino.model.AvatarSource;
import vino.model.User;
import vino.model.UserAvatar;
import vino.utils.EmailUtils;
import vino.utils.ImageUtils;
import vino.utils.WebUtils;

@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class UserController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
	@Override
	protected String basePath() { return "user"; }
	
	@Override
	protected void initActions() {
		addAction("/", new Profile());
		addAction("/*", new View());
		addAction("/new", new New());
		addAction("/create", new Create());
		addAction("/avatar-upload", new AvatarUpload());
		addAction("/avatar-source/*", new ModifyAvatarSource());
		addAction("/change-password", new ChangePassword());
		addAction("/forgot", new Forgot());
		addAction("/reset", new Reset());
	}

	@Override
	protected Action defaultAction() {
		return new Profile();
	}
	
	public class Profile extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser == null) {
				return "error:403"; // this condition should never happen
			}
			
			String username = (String) currentUser.getPrincipal();
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404"; // this condition should never happen
			}
			
			request.setAttribute("user", user);
			
			return basePath() + "/profile.jsp";
		}		
	}
	
	public class View extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String username = getRouteParameter(1);
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404";
			}
			
			return "text:Show users profile with name = "+username+", "+user;
		}		
	}
	
	public class New extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return basePath() + "/form.jsp";
		}
	}
	
	public class Create extends Action {
		
		public boolean supportsGet() { return false; }
		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params  = new Params(request);
							
			String username = params.getString("username");
			String password1 = params.getString("password_1");
			String password2 = params.getString("password_2");
			String email = params.getString("email");
		
			List<String> errors = new ArrayList<String>(0);

            if(username == null) {
                errors.add("Username is required");
            }                
            else {
            		username = username.toLowerCase();
            		if (!username.matches("^[a-z0-9]*$")) errors.add("Username must be alphanumeric");
                    if (username.length() < 4 || username.length() > 16) errors.add("Username must be between 4 and 16 characters");                        
                    if (username.equals("admin")) errors.add("That username already exists!");
                    if (User.find("username = ?", username).size() > 0) errors.add("That username already exists!");
            }
                        
            if(password1 == null) {
                    errors.add("Password is required");
            }
            else {
                    if (password1.length() < 4 || password1.length() > 32) errors.add("Password must be between 4 and 32 characters");
            }
                        
            if(password2 == null || !password1.equals(password2)) {
                    errors.add("Passwords do not match");
            }
        
            if(email == null) {
                    errors.add("Email is required");
            }
            else {
                    email = email.toLowerCase();
                    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) errors.add("Email appears to be invalid format");
                    if (User.find("email = ?", email).size() > 0) errors.add("A user already has that email address!");
            }
        
            if (errors.isEmpty()) {
    			User user = new User();
                user.setActive(true);
                user.setEmail(email);
                user.setPassword(password1);
                user.setUsername(username);
                user.save();
                
                log.debug("created a new user "+user.getUsername());
                
        		EmailUtils.sendSimpleMessage(email, "Welcome to Another Glass", "Thanks for joining our wine community!");
                
        		return "redirect:/login.jsp";
            }
            
            request.setAttribute("errors", errors);
			request.setAttribute("username", username);
			request.setAttribute("password_1", password1);
			request.setAttribute("password_2", password2);
			request.setAttribute("email", email);					
			
			return basePath() + "/form.jsp";
		}
	}
	
	public class AvatarUpload extends Action {

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser == null) {
				return "error:403"; // this condition should never happen
			}
			
			String username = (String) currentUser.getPrincipal();
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404"; // this condition should never happen
			}
			
			List<String> errors = new ArrayList<String>(0);
			
			if (params.isPost()) {
				String contentType = params.getFileUploadContentType("file");
				log.debug("found a "+contentType+" file");
				
				String name = params.getFileUploadName("file");
				log.debug("named "+name);
				
				byte[] file = params.getFileUploadBytes("file");				
				if (file != null) log.debug("of "+file.length+" bytes in size");
				
				if (contentType != null && name != null && file != null) {					
					if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".png")) {						
						if (contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/gif") || contentType.equalsIgnoreCase("image/png")) {
							if (file.length >= 1 && file.length <= 2*1024*1024) {
								byte[] data = ImageUtils.processUserUploadedAvatar(file);
								if (data != null) {
									UserAvatar userAvatar = UserAvatar.findFirst("user_id = ?", user.getId());
									if (userAvatar == null) {									
										userAvatar = new UserAvatar();
									}
									
									userAvatar.setContentType("image/jpeg");
									userAvatar.setData(data);
									userAvatar.setUser(user);
									userAvatar.save();
									
									user.setAvatarSource(AvatarSource.LOCAL);
									user.setUserAvatar(userAvatar);
									user.save();
									
									return "redirect:/user";
								}
								else {
									errors.add("The file did not upload properly :|");
								}
							}
							else {
								errors.add("The file is too large. We only accept avatar images that are less than 2 MB.");
							}
						}
						else {
							errors.add("The file doesn't appear to be a JPEG, GIF, or PNG image");
						}						
					}
					else {
						errors.add("The file should be .jpg, .jpeg, .gif, or .png");
					}					
				}
				else {
					errors.add("The file did not upload properly :(");
				}
			}

            request.setAttribute("errors", errors);
			
			return basePath() + "/avatar-upload.jsp";
		}
		
	}

	public class ModifyAvatarSource extends Action {

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String avatarSource = getRouteParameter(1);
			
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser == null) {
				return "error:403"; // this condition should never happen
			}
			
			String username = (String) currentUser.getPrincipal();
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404"; // this condition should never happen
			}
			
			user.setAvatarSource(AvatarSource.lookup(avatarSource));
			user.save();
			
			return "redirect:/user";
		}
	}
	
	public class ChangePassword extends Action {

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);

			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser == null) {
				return "error:403"; // this condition should never happen
			}
			
			String username = (String) currentUser.getPrincipal();
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404"; // this condition should never happen
			}
			
			if (params.isPost()) {
				String password1 = params.getString("password_1");
				String password2 = params.getString("password_2");

				List<String> errors = new ArrayList<String>(0);

	            if (password1 == null) {
                    errors.add("Password is required");
	            }
	            else {
	                    if (password1.length() < 4 || password1.length() > 32) errors.add("Password must be between 4 and 32 characters");
	            }
	                        
	            if(password2 == null || !password1.equals(password2)) {
	                    errors.add("Passwords do not match");
	            }
	            
	            if (errors.isEmpty()) {
	                user.setPassword(password1);
	                user.save();
	                
	                log.debug("changed password for user "+user.getUsername());
	                
	        		EmailUtils.sendSimpleMessage(user.getEmail(), "Another Glass Password Updated", "Hi "+user.getUsername()+", This is friendly reminder that you just changed your password on Another Glass. If you didn't intend to do this, please contact us at support@anotherglass.net so we can look into this for you!");
	                
	        		request.getSession().setAttribute("flash", "Your password was successfully updated!");
	        		
					return "redirect:/user";
	            }
	            
	            request.setAttribute("errors", errors);
			}
			
			return basePath() + "/change-password.jsp";
		}
	}
	
	public class Forgot extends Action {

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			
			String email = params.getString("email");

			List<String> errors = new ArrayList<String>(0);

			if (params.isPost()) {
				User user = User.findFirst("username = ?", email);
				
				if (user == null) {
					user = User.findFirst("email = ?", email);
					if (user == null) {
						errors.add("Sorry, we couldn't find a user with that username or email address");
					}
				}
				
				if (user != null) {
					user.initReset();
					user.save();
					
					String resetUrl = WebUtils.baseUrl(request) + "/user/reset?token="+user.getResetToken();

	        		EmailUtils.sendSimpleMessage(user.getEmail(), "Another Glass Password Reset", "Hi "+user.getUsername()+", Please continue with resetting your password by clicking this link: "+resetUrl+"\n\nIf you didn't intend to reset your password, please ignore this message.");

					request.getSession().setAttribute("flash", "Expect an email shortly to initiate a password reset");			
					return "redirect:/login.jsp";
				}
			}

			request.setAttribute("email", email);
            request.setAttribute("errors", errors);
			
			return basePath() + "/forgot.jsp";
		}
	}
	
	public class Reset extends Action {

		@Override
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			Params params = new Params(request);
			
			String token = params.getString("token");

			List<String> errors = new ArrayList<String>(0);

			User user = User.findFirst("reset_token = ?", token);
			if (user != null && user.getResetExpiration().after(new Date())) {
				String password1 = params.getString("password_1");
				String password2 = params.getString("password_2");

	            if (password1 == null) {
                    errors.add("Password is required");
	            }
	            else {
	                    if (password1.length() < 4 || password1.length() > 32) errors.add("Password must be between 4 and 32 characters");
	            }
	                        
	            if(password2 == null || !password1.equals(password2)) {
	                    errors.add("Passwords do not match");
	            }
	            
	            if (errors.isEmpty()) {
	                user.setPassword(password1);
	                user.clearReset();
	                user.save();
	                
	                log.debug("changed password for user "+user.getUsername());
	                
	        		EmailUtils.sendSimpleMessage(user.getEmail(), "Another Glass Password Updated", "Hi "+user.getUsername()+", This is friendly reminder that you just changed your password on Another Glass. If you didn't intend to do this, please contact us at support@anotherglass.net so we can look into this for you!");
	                
	        		request.getSession().setAttribute("flash", "Your password was successfully updated!");
	        		
					return "redirect:/user";
	            }					
			}
			else {
        		request.getSession().setAttribute("flash", "This reset link has expired. Please initiate a new one or contact support@anotherglass.net if you are having trouble resetting your password.");
				
				return "redirect:/login.jsp";
			}
			
			request.setAttribute("token", token);
            request.setAttribute("errors", errors);
			
			return basePath() + "/reset.jsp";
		}
	}	
	
}
