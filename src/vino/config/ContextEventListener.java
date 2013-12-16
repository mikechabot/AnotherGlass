package vino.config;

import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import vino.JobManager;
import vino.config.Configuration;
import vino.config.Version;
import vino.database.Query;


public class ContextEventListener implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(ContextEventListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
    	
    	ServletContext c = sce.getServletContext();
        String path = c.getRealPath("WEB-INF/etc");
                
        try {
            Version.load();
            log.info("AnotherGlass, v" + Version.display());

            Configuration conf = Configuration.getInstance();
            log.info("Loaded config.xml "); 
            
            // Verify database connectivity
            String sql = "select version()";
            Query query = new Query();           
            ResultSet resultSet =  query.execute(sql);
            if (resultSet.next()) {
                log.info("Database reached: " + resultSet.getString(1));
            }
            query.close();
            
            // Get job information
            JobManager manager = JobManager.getInstance();
            manager.populateJobsMap();
            log.info("Found " + manager.getJobsCount() + " jobs");
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
}