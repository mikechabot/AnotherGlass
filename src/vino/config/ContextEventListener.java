package vino.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import vino.config.Configuration;
import vino.config.Version;
import vino.database.Database;
import vino.job.JobManager;

public class ContextEventListener implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(ContextEventListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {    	        
        try {
        	ServletContext c = sce.getServletContext();
            String path = c.getRealPath(".");

        	log.info("Path is "+path);
	
            Version.load();
            log.info("AnotherGlass, v" + Version.display());

            Configuration.getInstance();
            log.info("Loaded config.xml "); 

            verifyDatabaseConnectivity();
            initJobsManager();            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
    
    private void verifyDatabaseConnectivity() throws SQLException {
        Database db = null;
    	try {        
	        db = new Database();
	        db.open();
	        
	        PreparedStatement stmt = db.getPreparedStatement("select version()");
	        ResultSet rs =  stmt.executeQuery();
	        if (rs.next()) {
	            log.info("Database reached: " + rs.getString(1));
	        }
	        
	        rs.close();
	        rs = null;
	        
	        stmt.close();
	        stmt = null;
    	}
    	finally {
    		if (db != null) {
    			db.close();
    		}
    	}    	
    }
    
    private void initJobsManager() {
        JobManager manager = JobManager.getInstance();
        manager.populateJobsMap();
        log.info("Found " + manager.getJobsCount() + " jobs");
    }
}