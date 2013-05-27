package com.anotherglass.solr;

import org.apache.log4j.Logger;

import com.anotherglass.config.Configuration;
import com.anotherglass.config.Configuration.ConfigurationException;
import com.anotherglass.http.HttpException;
import com.anotherglass.http.HttpHelper;

public class ImportWines {

	private static final Logger log = Logger.getLogger(ImportWines.class);
	
	public static void run() {
				
		Configuration config = Configuration.getInstance();
		try {
			config.initialize("web/WEB-INF/etc");
		}
		catch (ConfigurationException e) {
			log.error("Could not load configuration for some reason", e);
		}		
		
		String url = config.getSolrUrl() + "/dataimport?command=full-import";		
		HttpHelper httpHelper = new HttpHelper();
		
		try {
			log.info("Populating Solr with address" + url);
			httpHelper.get(url);
		} 
		catch (HttpException e) {
			log.error("Could not execute Solr import command for some reason", e);
		}			
	}
}
