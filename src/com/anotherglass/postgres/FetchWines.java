package com.anotherglass.postgres;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import com.anotherglass.config.Configuration;
import com.anotherglass.config.Configuration.ConfigurationException;
import com.anotherglass.http.HttpException;
import com.anotherglass.http.HttpHelper;
import com.anotherglass.wine.Products;
import com.anotherglass.wine.Status;
import com.anotherglass.wine.Wine;

public class FetchWines {	
	
	private static Logger log = Logger.getLogger(FetchWines.class);	
	
	public static List<Wine> fetch() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} 
		catch (ConfigurationException e) {
			log.error("Could not load configuration for some reason", e);
		}
		
		String wineUrl = config.getRequiredString("wineUrl");
		String apiKey = config.getRequiredString("apiKey");
		String fetchSize = config.getRequiredString("fetchSize");
		String fetchOffset = config.getOptionalString("fetchOffset", "0");
		
		HttpHelper httpHelper = new HttpHelper();
		
		List<Wine> wines = new ArrayList<Wine>(0);
		List<Wine> temp;
		Response response;
				
		String json = null;

		log.info("Starting fetch from " + wineUrl);
		do {
			try {
				String url = wineUrl + "catalog?size=" + fetchSize + "&apiKey=" + apiKey + "&offset=" + fetchOffset;
				json = httpHelper.get(url);
			} catch (HttpException e) {
				log.error("Error accessing server for some reason", e);				
			}
			response = new Gson().fromJson(json, Response.class);			
			temp = response.getProducts().getList();
			wines.addAll(temp);
			fetchOffset+=fetchSize;
		} while (!temp.isEmpty());
		
		log.info("Completed fetch");
		return wines;
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
