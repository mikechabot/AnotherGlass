package vino.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.job.Job;
import vino.model.Wine;
import vino.service.WineApiService;
import vino.service.WineDatabaseService;
import vino.utils.DateUtils;


public class WineApiJob extends Job implements Runnable  {
	
	private static Logger log = Logger.getLogger(WineApiJob.class);
	
	private static Thread thread;
	private static WineApiJob fetchJob;
	private static String lastRunTime;
	private static boolean running;
	
	
	// Disallow fetching more than once at the same time
    public static WineApiJob getInstance() {
        if (fetchJob == null) {
        	fetchJob = new WineApiJob();
        }
        return fetchJob;
    }
	
    public void start() { 
    	if (thread == null) {
    		thread = new Thread(this);
        	thread.start();
        } 
    } 
    
    public void stop() {
    	running = false;
    	thread = null;
    	log.info("Stopping thread, " + getJobName());
    }
    	
	@Override
	public void run() {
		running = true;
		log.info("Starting thread, " + getJobName());
		lastRunTime = DateUtils.getFormattedNow();
        while (running) {
			WineDatabaseService.dropAndCreateWinesTable();				
			List<Wine> wines = new ArrayList<Wine>(WineApiService.fetch());
			if(!wines.isEmpty()) {
				WineDatabaseService.insertWines(wines);	
			} else {
				log.warn("No wines were inserted; empty list returned from FetchWinesFromApiService.fetch()");
			}
			stop();
        }     
	}
	
	@Override
	public String getJobServletUrl() {
		return "/admin/fetch";
	} 	

	@Override
	public String getJobName() {
		return WineApiJob.class.getSimpleName();
	}

	@Override
	public String getJobDescription() {
		return "Fetch wine information from the wine.com API";
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public String getLastRunTime() {
		return lastRunTime;
	}	

}