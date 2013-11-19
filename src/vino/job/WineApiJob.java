package vino.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import vino.job.Job;
import vino.model.Wine;
import vino.service.ApiService;
import vino.service.DatabaseService;
import vino.utils.DateUtils;


public class WineApiJob extends Job implements Runnable  {
	
	private static Logger log = Logger.getLogger(WineApiJob.class);
	
	private static Thread thread;
	private static WineApiJob fetchJob;
	private static long runDate;
	private static long stopDate;
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
		runDate = new Date().getTime();
        while (running) {
			DatabaseService.dropAndCreateWinesTable();				
			List<Wine> wines = new ArrayList<Wine>(ApiService.fetch());
			if(!wines.isEmpty()) {
				DatabaseService.insertWines(wines);	
			} else {
				log.warn("No wines were inserted; empty list returned from FetchWinesFromApiService.fetch()");
			}
			stop();
			stopDate = new Date().getTime();
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
	public String getLastRunDate() {
		return runDate == 0 ? null : DateUtils.getFormattedDateFromLong(runDate);
	}

	@Override
	public String getLastRunDuration() {
		if(running) {
		  return DateUtils.getFormattedSpecial((new Date().getTime()-runDate));
		}
		return DateUtils.getFormattedSpecial((stopDate-runDate));
	}

}