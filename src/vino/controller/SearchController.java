package vino.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.Params;
import vino.Query;
import vino.model.SearchResult;
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
            
            Query query = new Query(params.getString("q"), params.getString("type"));
            
            if (query.getType() != null && query.getType().length() > 0) {
            	if (query.getType().equals("vineyards")) {
            		List<SearchResult> vineyards = new ArrayList<SearchResult>();
                	vineyards = DatabaseService.searchVineyards(query);
                	query.setResults(vineyards);
            	} else if (query.getType().equals("regions")) {
              		List<SearchResult> regions = new ArrayList<SearchResult>();
              		regions = DatabaseService.searchRegions(query);
              		query.setResults(regions);
            	}
            } else if (query.getQuery() != null && query.getQuery().length() > 0) {
                List<SearchResult> wines = new ArrayList<SearchResult>();
            	wines = DatabaseService.searchWines(query);
            	query.setResults(wines);
            }
            request.setAttribute("query", query);
			return basePath() + "/index.jsp";
		}		
	}
	
}