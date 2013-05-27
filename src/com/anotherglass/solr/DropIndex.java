package com.anotherglass.solr;

import org.apache.log4j.Logger;

import com.anotherglass.config.Configuration;
import com.anotherglass.config.Configuration.ConfigurationException;
import com.anotherglass.http.HttpException;
import com.anotherglass.http.HttpHelper;

public class DropIndex {

	private static final Logger log = Logger.getLogger(DropIndex.class);
	
	public static void drop() {
				
		Configuration config = Configuration.getInstance();
		try {
			config.initialize("web/WEB-INF/etc");
		}
		catch (ConfigurationException e) {
			log.error("Could not load configuration for some reason", e);
		}		
		
		HttpHelper httpHelper = new HttpHelper();
		String url = config.getSolrUrl() + "/update?stream.body=<delete><query>*:*</query></delete>&commit=true";
			
		try {
			log.info("Populating Solr with address" + url);
			httpHelper.get(url);
		} 
		catch (HttpException e) {
			log.error("Could not delete Solr documents for some reason", e);
		}	
		
	}
}
