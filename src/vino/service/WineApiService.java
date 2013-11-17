package vino.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.config.Configuration;
import vino.config.Configuration.ConfigurationException;
import vino.http.HttpException;
import vino.http.HttpHelper;
import vino.model.Products;
import vino.model.Status;
import vino.model.Wine;

import com.google.gson.Gson;


public class WineApiService {	
	
	private static Logger log = Logger.getLogger(WineApiService.class);	
	
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
		int fetchSize = config.getOptionalInt("fetchSize", 400);
		int fetchOffset = config.getOptionalInt("fetchOffset", 0);
		
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
			log.info("Fetched " + temp.size() + " wines");
			fetchOffset+=fetchSize;
		} while (!temp.isEmpty());
		
		log.info("Fetch complete, got " + wines.size() + " wines");
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
