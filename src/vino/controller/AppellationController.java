package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;
import vino.model.Appellation;
import vino.utils.StringUtils;

public class AppellationController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(AppellationController.class);
	
	@Override
	protected String basePath() { return "appellation"; }
	
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
			return "text:Show a list of Appellations";
		}		
	}
	
	public class View extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
			long id = StringUtils.parseLong(getRouteParameter(1));
			if (id < 0) {
				return "error:404";
			}
			
			Appellation appellation = Appellation.findById(id);
			if (appellation == null) {
				return "error:404";
			}
			
			request.setAttribute("appellation", appellation);			

			return basePath() + "/profile.jsp";
		}		
	}
	
	public class New extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:New appellation";
		}
	}
	
	public class Create extends Action {				
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Create appellation";
		}
	}
	
	public class Edit extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Edit form for appellation where the id = "+getRouteParameter(1);
		}
	}
	
	public class Update extends Action {		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Update data for appellation where the id = "+getRouteParameter(1);
		}
	}
	
	public class Delete extends Action {		
		public boolean supportsGet() { return false; }
		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {						
			return "text:Delete appellation where the id = "+getRouteParameter(1);
		}
	}
	
}