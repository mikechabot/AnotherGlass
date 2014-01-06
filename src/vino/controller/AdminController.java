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
	protected String basePath() { return "admin"; }	
	
	@Override
	protected void initActions() {
		addAction("/", new GetJobInformation());
		addAction("/populate", new RunWinePopulatorJob());
	}

	@Override
	protected Action defaultAction() {
		return new GetJobInformation();
	}
	
	public class GetJobInformation extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
			request.setAttribute("jobs", manager.getJobs());
			request.setAttribute("running", manager.getRunningJobCount());
			return basePath() + "/index.jsp";
		}		
	}
	
	public class RunWinePopulatorJob extends Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// Start a fetch job if it's not already running
			WinePopulatorJob job = WinePopulatorJob.getInstance();
			if (!job.isRunning()) {
				job.start();
			}
			return "redirect:/admin";
		}		
	}
	
}
