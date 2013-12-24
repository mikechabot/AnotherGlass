package vino.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import vino.job.Job;
import vino.populator.ApiService;
import vino.populator.Wine;

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
        	
        	Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/dev", "anotherglass", "anotherglass");
        	
        	for(Wine each : ApiService.fetch()) {
        		vino.model.Wine wine = new vino.model.Wine();        		
    			wine.setName(each.getName());
    			wine.setWinesComId(each.getId());
    			wine.setDescription(each.getDescription());
    			wine.setType(each.getType());
    			wine.setVintage(each.getVintage());
    			
    			if (each.getAppellation() != null) {
	    			LazyList<vino.model.Appellation> appellationList = vino.model.Appellation.find("wines_com_id = ?", each.getAppellation().getId());
	    			if (appellationList != null && appellationList.size() >= 1) {
	    				wine.setAppellation(appellationList.get(0));
	    			}
	    			else {
	    				vino.model.Appellation appellation = new vino.model.Appellation();
	    				appellation.setWinesComId(each.getAppellation().getId());
	    				appellation.setName(each.getAppellation().getName());
	    				
	        			if (each.getAppellation().getRegion() != null) {
	    	    			LazyList<vino.model.Region> regionList = vino.model.Region.find("wines_com_id = ?", each.getAppellation().getRegion().getId());
	    	    			if (regionList != null && regionList.size() >= 1) {
	    	    				appellation.setRegion(regionList.get(0));	    	    				
	    	    			}
	    	    			else {
	    	    				vino.model.Region region = new vino.model.Region();
	    	    				region.setWinesComId(each.getAppellation().getRegion().getId());
	    	    				region.setName(each.getAppellation().getRegion().getName());
	    	    				region.save();
	    	    				appellation.setRegion(region);
	    	    			}
	        			}
	        			
	        			appellation.save();
	        			
	        			wine.setAppellation(appellation);
	        			wine.setRegion(appellation.getRegion());
	    			}
    			}
    			
    			if (each.getVineyard() != null) {
	    			LazyList<vino.model.Vineyard> vineyardList = vino.model.Vineyard.find("wines_com_id = ?", each.getVineyard().getId());
	    			if (vineyardList != null && vineyardList.size() >= 1) {
	    				wine.setVineyard(vineyardList.get(0));
	    			}
	    			else {
	    				vino.model.Vineyard vineyard = new vino.model.Vineyard();
	    				vineyard.setWinesComId(each.getVineyard().getId());
	    				vineyard.setName(each.getVineyard().getName());
	    				vineyard.setAppellation(wine.getAppellation());
	    				vineyard.save();
	    				wine.setVineyard(vineyard);
	    			}
    			}
    			
        		wine.save();
        	}
        	
        	Base.close();
        	
			stop();
			stopDate = new Date().getTime();
        }     
	}
	
	@Override
	public String getJobServletUrl() {
		return "/admin/populate";
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
		return runDate == 0 ? null : new DateTime(runDate).toString("MM/dd/yyyy h:mm a");
	}

	@Override
	public String getLastRunDuration() {
		DateTime start = new DateTime(runDate);
		DateTime stop = (running) ? new DateTime() : new DateTime(stopDate);
		
		Period period = new Period(start, stop);

		PeriodFormatter formatter = new PeriodFormatterBuilder()
		    .appendSeconds().appendSuffix("s ")
		    .appendMinutes().appendSuffix("m ")
		    .appendHours().appendSuffix("h ")
		    .printZeroNever()
		    .toFormatter();

		return formatter.print(period);		
	}

}