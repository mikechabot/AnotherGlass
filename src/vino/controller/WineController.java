package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.model.Wine;
import vino.service.WineDatabaseService;

public class WineController extends Controller {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return ""; }	
	
	@Override
	protected void initActions() {
		addAction(null, new GetAction());
	}

	@Override
	protected Action defaultAction() {
		return new GetAction();
	}
	
	public class GetAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String action = request.getPathInfo();
		if(action == null || action.equals("") || action.equals("/")) {					
			request.setAttribute("wines", WineDatabaseService.fetchWines());			
		} else if (action.matches("\\/([0-9]+)$")) {			
			Wine wine = WineDatabaseService.fetchWine(action.replaceAll("/", ""));
			if (wine != null) {
				request.setAttribute("wine", wine);	
			} else {
				response.sendError(404);
				return null;
			}					
		}
		return basePath() + "/index.jsp";
		}		
	}
	
}