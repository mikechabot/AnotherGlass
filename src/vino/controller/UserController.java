package vino.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import vino.Controller;
import vino.Params;
import vino.model.User;
import vino.utils.EmailUtils;

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
			
			return "text:Show current users ["+username+"] profile, "+user;
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
	
}
