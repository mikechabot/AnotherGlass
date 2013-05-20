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
	
	public static void main(String[] args) {		
		new FetchWines().fetch();
	}
	
	public List<Wine> fetch() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} 
		catch (ConfigurationException e) {
			log.error("Could not load configuration for some reason", e);
		}
		
		String wineUrl = config.getRequiredString("wineUrl");
		String apiKey = config.getRequiredString("apiKey");
		
		HttpHelper httpHelper = new HttpHelper();
		
		List<Wine> wines = new ArrayList<Wine>(0);
		List<Wine> temp;
		Response response;
				
		String json = null;
		int size = 400;
		long offset = 0;		

		log.info("Starting fetch");
		do {
			try {
				String url = wineUrl + "catalog?size=" + size + "&apiKey=" + apiKey + "&offset=" + offset;
				json = httpHelper.get(url);
			} catch (HttpException e) {
				log.error("Error accessing server for some reason", e);				
			}
			response = new Gson().fromJson(json, Response.class);			
			temp = response.getProducts().getList();
			wines.addAll(temp);
			offset+=size;
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
