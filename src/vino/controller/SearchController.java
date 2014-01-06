package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.Params;
import vino.model.Appellation;
import vino.model.Vineyard;
import vino.model.Wine;

public class SearchController extends Controller {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected String basePath() { return "search"; }	
	
	@Override
	protected void initActions() {
		addAction("/", new GetAction());
	}

	@Override
	protected Action defaultAction() {
		return new GetAction();
	}
	
	public class GetAction extends Action {
		
		public boolean supportsPost() { return false; }
		
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {			
            Params params = new Params(request);
                        
            String q = params.getString("q");
            String type = params.getString("type");
        
            if (q != null && type != null) {            	
            	if ("wines".equalsIgnoreCase(type)) {
            		request.setAttribute("results", Wine.find("name like ?", "%"+q+"%"));
            	}
            	else if ("vineyards".equalsIgnoreCase(type)) {
            		request.setAttribute("results", Vineyard.find("name like ?", "%"+q+"%"));
            	}
            	else if ("regions".equalsIgnoreCase(type)) {
            		request.setAttribute("results", Appellation.find("name like ?", "%"+q+"%"));
            	}    	
            }
            
            request.setAttribute("query", q);
            request.setAttribute("type", type);
            
			return basePath() + "/index.jsp";
		}		
	}
	
}