package com.anotherglass.postgres;

import java.util.ArrayList;
import java.util.List;

import com.anotherglass.wine.Wine;

public class Manager {

	private static Manager manager;
	private static boolean running;
	private static boolean success;
	
	public static Manager getInstance() {
		if(manager == null) {
			manager = new Manager();
		}
		return manager;
	}
	
	public Manager() {
		running = false;
		success = false;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void runImport() {
		
		if(!running) {
			running = true;
			
			CreateWinesTable wineTable = new CreateWinesTable();
			FetchWines fetchCatalog = new FetchWines();
			InsertWines insertWines = new InsertWines();
			QueryWines queryWines = new QueryWines();
			
			List<Wine> wines = new ArrayList<Wine>();
			
			wineTable.create();
			wines = fetchCatalog.fetch();
			if(!wines.isEmpty()) {
				insertWines.insert(wines);	
			}
			
			if(queryWines.query() > 0) success = true; 
			running = false;
		}
	}	
}

