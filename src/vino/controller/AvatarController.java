package vino.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;
import vino.model.User;
import vino.model.UserAvatar;

public class AvatarController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(AvatarController.class);
	
	@Override
	protected String basePath() { return "avatar"; }
	
	@Override
	protected void initActions() {
		addAction("/*", new View());
	}

	@Override
	protected Action defaultAction() {
		return new View();
	}
		
	public class View extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			String username = getRouteParameter(1);
			if (username != null && username.endsWith(".jpg")) {
				username = username.substring(0, username.length()-4);
			}
			
			log.debug("loading local avatar image for "+username);
			
			User user = User.findFirst("username = ?", username);
			if (user == null) {
				return "error:404";
			}
			
			UserAvatar avatar = UserAvatar.findFirst("user_id = ?", user.getId());
			if (avatar == null) {
				return "error:404";
			}

			response.setContentType(avatar.getContentType());
			OutputStream out = response.getOutputStream();
			out.write(avatar.getData(), 0, avatar.getData().length);
			out.flush();
			
			return null;
		}
	}
	
}
