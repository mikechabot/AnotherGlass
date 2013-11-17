package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import vino.Controller;
import vino.JobManager;
import vino.job.WineApiJob;


public class AdminController extends Controller {

	private static final long serialVersionUID = 1L;
	
	JobManager manager = JobManager.getInstance();

	@Override
	protected String basePath() { return ""; }	
	
	@Override
	protected void initActions() {
		addAction(null, new GetJobInformation());
		addAction("/fetch", new RunFetchWineJob());
	}

	@Override
	protected Action defaultAction() {
		return new GetJobInformation();
	}
	
	public class GetJobInformation implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
			request.setAttribute("jobs", manager.getJobs());
			request.setAttribute("running", manager.getRunningJobCount());
			return basePath() + "/admin.jsp";
		}		
	}
	
	public class RunFetchWineJob implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			// Start a fetch job if it's not already running
			WineApiJob fetchJob = WineApiJob.getInstance();
			if (!fetchJob.isRunning()) {
				fetchJob.start();				
			}

			response.sendRedirect("/admin");
			return null;
		}		
	}
	
}
