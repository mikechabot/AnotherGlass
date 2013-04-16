package com.anotherglass.solr.worker;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.anotherglass.http.HttpException;
import com.anotherglass.http.HttpHelper;
import com.anotherglass.utils.Configuration;
import com.anotherglass.utils.Configuration.ConfigurationException;

import com.sun.org.apache.regexp.internal.RE;

public class ImportCatalog {

	private static Logger log = Logger.getLogger(ImportCatalog.class);
	
	public static void main(String[] args) {
		new ImportCatalog().run();
	}
	
	public void run() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize("web/WEB-INF/etc");
		} 
		catch (ConfigurationException e) {
			log.error("could not load configuration for some reason", e);
		}

		String solrHome = config.getRequiredString("solrHome");
		String solrUrl = config.getRequiredString("solrUrl");
		String updateUrl = solrUrl + "/update";
		String catalogPath = solrHome + "/catalog";
		
		HttpHelper httpHelper = new HttpHelper();
		
		try {
			
			File catalog = new File(catalogPath);
			File[] files = catalog.listFiles(new SolrJsonDocumentFilter());
						
			if (files != null && files.length > 0) {	
				log.debug("found "+files.length+" documents");
				for(File file : files) {						
					log.debug("importing: "+file.getAbsolutePath());
					
					try {
						log.debug(file.getAbsoluteFile());
						httpHelper.post(updateUrl, "stream.file="+file.getAbsolutePath()+"&stream.contentType=application/json&commit=true");
					}
					catch (HttpException e) {
						log.error("unable to add to SOLR index", e);
						continue;
					}
					
					// move the file to the processed folder
					File processedPath = new File(catalogPath + "/processed/");
					if (!processedPath.exists()) {
						processedPath.mkdirs();
					}						
					if (!file.renameTo(new File(processedPath, file.getName()))) {
						throw new IOException("error moving file to processed directory");
					}					
				}				
			}
			else {
				log.debug("checked, but no documents were found");
			}
			
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}		
		
	}
	
	public class SolrJsonDocumentFilter implements FileFilter {

		// expecting .json files
		RE re = new RE(".json$");
		
		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				log.trace("skipping "+file.getName()+" because it is a directory");
				return false;
			}
			
			if (!re.match(file.getName())) {
				log.trace("skipping "+file.getName()+" because it isn't in the correct naming convention");
				return false;
			}
			
			if (file.length() <= 0) {
				log.trace("skipping "+file.getName()+" because it is empty");
			}
			
			if (!file.exists()) {
				log.trace("skipping "+file.getName()+" because it no longer exists");
				return false;
			}
			
			return true;
		}
		
	}	

}
