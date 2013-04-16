package com.anotherglass.solr;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.anotherglass.utils.Configuration;
import com.anotherglass.utils.Version;

public class ContextEventListener implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(ContextEventListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext c = sce.getServletContext();
        String path = c.getRealPath("WEB-INF/etc");
                
        try {
            Version.load();
            log.info("AnotherGlass.com, v" + Version.display());
            
            Configuration conf = Configuration.getInstance();
            conf.initialize(path);
            log.info("Loaded glass.properties (" + path + ")");
            
            String solrHome = conf.getSolrHome();
            System.setProperty("solr.solr.home", solrHome);           
            log.info("SOLR Home has been set to: "+ solrHome);
    	
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	//        
    }
}