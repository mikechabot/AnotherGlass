package vino.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.Params;
import vino.model.Wine;
import vino.service.DatabaseService;

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
            
            // Send back a list of Wine objects
            List<Wine> wines = new ArrayList<Wine>();
            String query = params.getString("query");
            if (query != null && query.length() > 0) {
            	wines = DatabaseService.searchWines(query);
            	request.setAttribute("wines", wines);
            }
			
			return basePath() + "/index.jsp";
		}		
	}
	
}