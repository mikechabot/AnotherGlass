package com.anotherglass.vino.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.anotherglass.vino.Job;
import com.anotherglass.vino.model.Wine;
import com.anotherglass.vino.service.ApiService;
import com.anotherglass.vino.service.DatabaseService;

public class FetchFromApi extends Job implements Runnable  {
	
	private static Logger log = Logger.getLogger(FetchFromApi.class);
	
	private static FetchFromApi fetchJob;
	
	private static Thread thread;
	private static boolean running;	
	
	// Disallow fetching more than once at the same time
    public static FetchFromApi getInstance() {
        if (fetchJob == null) {
        	fetchJob = new FetchFromApi();
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
        while (running) {
			DatabaseService.dropAndCreateWinesTable();				
			List<Wine> wines = new ArrayList<Wine>(ApiService.fetch());
			if(!wines.isEmpty()) {
				DatabaseService.insertWines(wines);	
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
		return FetchFromApi.class.getSimpleName();
	}

	@Override
	public String getJobDescription() {
		return "Fetch wine information from the wine.com API";
	}

	@Override
	public boolean isRunning() {
		return running;
	}

}
