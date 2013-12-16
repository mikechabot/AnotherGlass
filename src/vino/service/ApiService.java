package vino.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.config.Configuration;
import vino.http.HttpException;
import vino.http.HttpHelper;
import vino.model.Products;
import vino.model.Status;
import vino.model.Wine;

import com.google.gson.Gson;


public class ApiService {	
	
	private static Logger log = Logger.getLogger(ApiService.class);	
	
	public static List<Wine> fetch() {
		
		Configuration config = Configuration.getInstance();
				
		HttpHelper httpHelper = new HttpHelper();
		
		List<Wine> wines = new ArrayList<Wine>(0);
		List<Wine> temp;
		Response response;
				
		String json = null;

		long fetchOffset = config.getWineApi().getFetchOffset();
		
		log.info("Starting fetch from " + config.getWineApi().getUrl());
		do {
			try {
				String url = config.getWineApi().getUrl() + "catalog?size=" + config.getWineApi().getFetchSize() + "&apiKey=" + config.getWineApi().getKey() + "&offset=" + fetchOffset;
				json = httpHelper.get(url);
				log.trace("json="+json);
			} catch (HttpException e) {
				log.error("Error accessing server for some reason", e);				
			}
			response = new Gson().fromJson(json, Response.class);			
			temp = response.getProducts().getList();
			wines.addAll(temp);
			log.info("Fetched " + temp.size() + " wines for a total of " + wines.size());
			fetchOffset += config.getWineApi().getFetchSize();
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
