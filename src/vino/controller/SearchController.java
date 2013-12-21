package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.Params;

public class SearchController extends Controller {
	
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
            Params params = new Params(request);
            
            // No POST requests allowed
            if (params.isPost()) {
            	response.sendRedirect(basePath() + "/index.jsp");
            	return null;
            }
            
            //TODO return search results
            
			return basePath() + "/index.jsp";
		}		
	}
	
}