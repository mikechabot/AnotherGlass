package com.anotherglass.solr.worker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import com.anotherglass.http.HttpException;
import com.anotherglass.http.HttpHelper;
import com.anotherglass.utils.Configuration;
import com.anotherglass.utils.Configuration.ConfigurationException;
import com.anotherglass.wine.Products;
import com.anotherglass.wine.Status;
import com.anotherglass.wine.Wine;

public class FetchCatalog {	
	
	private static Logger log = Logger.getLogger(FetchCatalog.class);
	
	public static void main(String[] args) {		
		new FetchCatalog().fetch();
	}
	
	public void fetch() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize("web/WEB-INF/etc");
		} 
		catch (ConfigurationException e) {
			log.error("could not load configuration for some reason", e);
		}
		
		String wineUrl = config.getRequiredString("wineUrl");
		String apiKey = config.getRequiredString("apiKey");
		String solrHome = config.getRequiredString("solrHome");
		String catalogPath = solrHome + "/catalog";
		
		HttpHelper httpHelper = new HttpHelper();
		List<Wine> wines;
		Response response;
				
		int querySize = 250;  // Fetch only 250 wines since the server is slow
		String json = null;		
		long offset = 0;
		long totalProducts = 0;	
		
		File path = new File(catalogPath);
		if(!path.exists()) path.mkdir();
		
		log.info("Starting fetch...");
		do {
			try {
				File temp = FileUtils.getFile(catalogPath + "/catalog-" + offset + ".json");
				String url = wineUrl + "catalog?size="+ querySize + "&apiKey=" + apiKey + "&offset=" + offset;
				json = httpHelper.get(url);				
				FileUtils.writeStringToFile(temp, json);
				log.info("Saved '" + temp.getAbsolutePath() + "'...");
			} catch (IOException e) {
				log.error("Error opening file for some reason", e);
			} catch (HttpException e) {
				log.error("Error accessing server for some reason", e);				
			}
			response = new Gson().fromJson(json, Response.class);
			totalProducts = response.getProducts().getTotal();
			wines = new ArrayList<Wine>(response.getProducts().getList());
			log.info("Fetched " + (int)(offset * 100.0 / totalProducts + 0.5) + "%, just pulled " + wines.size() + " wine(s)");
			offset+=querySize;				
		} while (!wines.isEmpty());
		log.info("Fetch complete");
	}
	
class Response {
		
		private Status Status;
		private Products Products;
		
		public Status getStatus() {
			return Status;
		}
	
		public void setStatus(Status status) {
			this.Status = status;
		}
		
		public Products getProducts() {
			return Products;
		}
		
		public void setProducts(Products products) {
			Products = products;
		}
	}
}
