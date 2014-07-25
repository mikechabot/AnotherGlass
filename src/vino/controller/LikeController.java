package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;
import vino.model.Activity;
import vino.model.ActivityType;
import vino.model.User;
import vino.model.Wine;
import vino.utils.ShiroUtils;
import vino.utils.StringUtils;

public class LikeController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(LikeController.class);
	
	@Override
	protected String basePath() { return "like"; }
	
	@Override
	protected void initActions() {
		addAction("/wine/*", new LikeWine());
	}

	@Override
	protected Action defaultAction() {
		return new NotFoundAction();
	}
		
	public class LikeWine extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			long id = StringUtils.parseLong(getRouteParameter(1));
						
			Wine wine = Wine.findById(id);
			if (wine == null) {
				return "error:404";
			}
			
			User user = ShiroUtils.getLoggedInUser();
			if (user == null) {
				return "error:401";
			}
			
			Activity activity = Activity.findFirst("type='like' and wine_id = ? and user_id = ?", id, user.getId());
			if (activity != null) {
				activity.setActive(!activity.isActive()); // if liked, unlike, if unliked, like
			}
			else {
				activity = new Activity();
				activity.setActive(true);
				activity.setType(ActivityType.LIKE);
				activity.setUser(user);
				activity.setWine(wine);
			}
			
			log.debug(user.getUsername()+" "+(activity.isActive() ? "liked" : "unliked")+" wine "+id);
			
			activity.save();
			
			return "redirect:/wine/"+id;
		}
	}
	
}
