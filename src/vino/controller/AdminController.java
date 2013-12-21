package vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vino.Controller;
import vino.job.JobManager;
import vino.job.WinePopulatorJob;

public class AdminController extends Controller {

	private static final long serialVersionUID = 1L;
	
	JobManager manager = JobManager.getInstance();

	@Override
	protected String basePath() { return ""; }	
	
	@Override
	protected void initActions() {
		addAction(null, new GetJobInformation());
		addAction("/populate", new RunWinePopulatorJob());
	}

	@Override
	protected Action defaultAction() {
		return new GetJobInformation();
	}
	
	public class GetJobInformation implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
			request.setAttribute("jobs", manager.getJobs());
			request.setAttribute("running", manager.getRunningJobCount());
			return basePath() + "admin/index.jsp";
		}		
	}
	
	public class RunWinePopulatorJob implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// Start a fetch job if it's not already running
			WinePopulatorJob job = WinePopulatorJob.getInstance();
			if (!job.isRunning()) {
				job.start();
			}
			response.sendRedirect("/admin");
			return null;
		}		
	}
	
}
