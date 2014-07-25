package vino.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import vino.Controller;
import vino.model.Activity;
import vino.model.User;
import vino.model.Wine;
import vino.utils.ShiroUtils;
import vino.utils.StringUtils;

public class WineController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(WineController.class);
	
	@Override
	protected String basePath() { return "wine"; }
	
	@Override
	protected void initActions() {
		addAction("/*", new View());
		addAction("/new", new New());
		addAction("/create", new Create());
		addAction("/edit/*", new Edit());
		addAction("/*/update", new Update());
		addAction("/*/delete", new Delete());
	}

	@Override
	protected Action defaultAction() {
		return new Index();
	}
	
	public class Index extends Action {			
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Show a list of Wines";
		}		
	}
	
	public class View extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			long id = StringUtils.parseLong(getRouteParameter(1));
			if (id < 0) {
				return "error:404";
			}
			
			Wine wine = Wine.findById(id);
			if (wine == null) {
				return "error:404";
			}
			
			request.setAttribute("wine", wine);
			
			List<Activity> likes = Activity.where("type = 'like' and wine_id = ? and active = true", id).orderBy("created_at asc").include(User.class).limit(25);
			request.setAttribute("likes", likes);

			boolean alreadyLiked = false;
			
			User user = ShiroUtils.getLoggedInUser();
			if (user != null) {
				Activity activity = Activity.findFirst("type = 'like' and wine_id = ? and user_id = ?", id, user.getId());
				if (activity != null) {
					alreadyLiked = activity.isActive();
				}
			}
			
			request.setAttribute("alreadyLiked", alreadyLiked);
			
			return basePath() + "/profile.jsp";
		}		
	}
	
	public class New extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:New wine";
		}
	}
	
	public class Create extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Create wine";
		}
	}
	
	public class Edit extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Edit form for wine where the id = "+getRouteParameter(1);
		}
	}
	
	public class Update extends Action {		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Update data for wine where the id = "+getRouteParameter(1);
		}
	}
	
	public class Delete extends Action {		
		public boolean supportsGet() { return false; }
		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Delete wine where the id = "+getRouteParameter(1);
		}
	}
	
}
