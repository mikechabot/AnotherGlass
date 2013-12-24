package vino.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;
import vino.Params;
import vino.model.User;

public class UserController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
	@Override
	protected String basePath() { return "user"; }	
	
	@Override
	protected void initActions() {
		addAction(null, new View());
		addAction("/signup", new SignUp());
		addAction("/create", new Create());
	}

	@Override
	protected Action defaultAction() {
		return new View();
	}
	
	public class View implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			// can't go here unless you are logged in
			response.sendError(403);
			
			return basePath() + "/user/profile.jsp";
		}		
	}
	
	public class SignUp implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return basePath() + "/signup.jsp";
		}
	}
	
	public class Create implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params  = new Params(request);
			
			if (params.isPost()) {
				
				List<String> errors = new ArrayList<String>(0);
				
				String username = params.getString("username");
				String password1 = params.getString("password_1");
				String password2 = params.getString("password_2");
				String email = params.getString("email");
				
                if(username == null) {
                    errors.add("Username is required");
                }
	            else {
	            		username = username.toLowerCase();
	            		if (!username.matches("^[a-z0-9]*$")) errors.add("Username must be alphanumeric");
	                    if (username.length() < 4 || username.length() > 16) errors.add("Username must be between 4 and 16 characters");                        
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
	                    
	                    response.sendRedirect("/login.jsp");
	                    return null;
	            }
				
	            request.setAttribute("errors", errors);
				request.setAttribute("username", username);
				request.setAttribute("password_1", password1);
				request.setAttribute("password_2", password2);
				request.setAttribute("email", email);
			}
			
			return basePath() + "/signup.jsp";
		}
	}
	
}
