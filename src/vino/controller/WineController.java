package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;

public class WineController extends Controller {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(UserController.class);
	
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
			return "text:Show wine where id = "+getRouteParameter(1);
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
