package com.anotherglass.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.anotherglass.utils.Configuration;
import com.anotherglass.utils.Version;

public class ContextEventListener implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(ContextEventListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
                
        try {
            Version.load();
            log.info("AnotherGlass, v" + Version.display());
            
            Configuration conf = Configuration.getInstance();
            conf.initialize();
            log.info("Loaded glass.properties ");        
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
}