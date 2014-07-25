package vino;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.utils.StringUtils;

public abstract class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(Controller.class);
	
	private Action defaultAction;
	private Map<String, Action> actions = new HashMap<String, Action>();
	
	String pathInfo = null;
	String route = null;
	String view = null;
	private List<String> routeParams;
	private Action action = null;
		
	public void init(ServletConfig config) throws ServletException {
		initActions();
		defaultAction = defaultAction();
		if (defaultAction == null) throw new ServletException("A default action was not specified");
		
		log.debug("Loaded Controller with Base Path of: /"+basePath());
	}
	
	protected abstract void initActions();
	protected abstract Action defaultAction();
	protected abstract String basePath();
		
	protected void routeAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathInfo = request.getPathInfo();
		routeParams = new ArrayList<String>(0);
		if (pathInfo != null && pathInfo.indexOf("/") > -1) {
			routeParams.addAll(Arrays.asList(pathInfo.split("/")));
		}
		else {
			pathInfo = "/";
		}
				
		log.debug("request = /"+basePath()+pathInfo+" ["+request.getMethod()+"]");
			
		try {
			if (pathInfo.equals("/")) {
				action = defaultAction;
			}
			else {
				action = actions.get(pathInfo);
				if (action == null) {
					/* I've coded for the following route options: 
					 *
					 *          /[:param1]
					 *          /[:param1]/action
					 *          /action/[:param1]
					 *          /action/[:param1]/[:param2]
					 *          /action/[:param1]/sub-action
					*/				
					for(String each : actions.keySet()) {
						if (each.equals("/*") && routeParams.size()-1 == 1) {
							route = each;
							action = actions.get(each);
							break;
						}
						else if (routeParams.size()-1 == 2 && each.equals("/*/"+routeParams.get(2))) {
							routeParams.remove(2);
							route = each;
							action = actions.get(each);
							break;						
						}
						else if (routeParams.size()-1 == 2 && each.equals("/"+routeParams.get(1)+"/*")) {
							routeParams.remove(1);
							route = each;
							action = actions.get(each);
							break;
						}
						else if (routeParams.size()-1 == 3 && each.equals("/"+routeParams.get(1)+"/*/*")) {
							routeParams.remove(1);
							route = each;
							action = actions.get(each);
							break;
						}
						else if (routeParams.size()-1 == 3 && each.equals("/"+routeParams.get(1)+"/*/"+routeParams.get(3))) {
							routeParams.remove(1);
							routeParams.remove(3);
							route = each;
							action = actions.get(each);
							break;
						}
					}
				}
			}
			
			log.debug("route = "+route);
			log.debug("routeParams = "+routeParams);
			
			if (action == null) {
				response.sendError(404);
				return;
			}
			
			log.debug("action = "+action.getClass());

			if ("GET".equals(request.getMethod()) && action.supportsGet() || "POST".equals(request.getMethod()) && action.supportsPost()) {
				view = action.execute(request, response);
			}
			else {
				log.debug("controller action does not respond to "+request.getMethod()+" requests");
				response.sendError(403);
				return;
			}
			
			log.debug("view = "+ view);
			
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
		
		if (view != null && !view.equals("")) {
			if (view.startsWith("text:")) {
				String text = view.substring("text:".length());
				response.getOutputStream().println(text);
			}
			else if (view.startsWith("redirect:")) {
				String redirect = view.substring("redirect:".length());
				response.sendRedirect(redirect);
			}
			else if (view.startsWith("error:")) {
				int error = StringUtils.parseInt(view.substring("error:".length()));
				response.sendError(error);
			}			
			else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/"+view);
				if (dispatcher == null) throw new ServletException("The view file (WEB-INF/views/"+view+") was not found!");			
				dispatcher.forward(request, response);
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		routeAction(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		routeAction(request, response);
	}
	
	public void addAction(String path, Action action) {
		actions.put(path, action);
	}
	
	public String getPathInfo() {
		return pathInfo;
	}
	
	public String getRoute() {
		return route;
	}
	
	public String getView() {
		return view;
	}
	
	public String getRouteParameter(int i) {
		if (routeParams != null && routeParams.size()-1 <= i) {
			return routeParams.get(i);
		}
		return null;
	}

	public abstract class Action {

		public boolean supportsGet() { 
			return true;
		}
		
		public boolean supportsPost() { 
			return true;
		}

		public abstract String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;		
	}
	
	public class NotFoundAction extends Action {

		public boolean supportsGet() { 
			return true;
		}
		
		public boolean supportsPost() { 
			return true;
		}

		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return "error:404";
		}
	}	
	
}