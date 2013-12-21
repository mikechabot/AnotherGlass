package vino.job;

import java.util.Date;

import org.apache.log4j.Logger;

import vino.job.Job;
import vino.utils.DateUtils;

public class WinePopulatorJob implements Job {
	
	private static Logger log = Logger.getLogger(WinePopulatorJob.class);
	
	private static Thread thread;
	private static WinePopulatorJob fetchJob;
	private static long runDate;
	private static long stopDate;
	private static boolean running;	
	
	// Disallow fetching more than once at the same time
    public static WinePopulatorJob getInstance() {
        if (fetchJob == null) {
        	fetchJob = new WinePopulatorJob();
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
        	//TODO update logic to populate tables
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
		return WinePopulatorJob.class.getSimpleName();
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