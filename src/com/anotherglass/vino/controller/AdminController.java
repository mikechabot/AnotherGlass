package com.anotherglass.vino.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.anotherglass.vino.Controller;
import com.anotherglass.vino.JobManager;
import com.anotherglass.vino.job.FetchFromApi;

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
			FetchFromApi fetchJob = FetchFromApi.getInstance();
			if (!fetchJob.isRunning()) {
				fetchJob.start();				
			}
			
			request.setAttribute("jobs", manager.getJobs());
			Thread.sleep(250);
			request.setAttribute("running", manager.getRunningJobCount());
			return basePath() + "/admin.jsp";
		}		
	}
	
}
