package com.anotherglass.utils;

import java.util.ArrayList;
import java.util.List;

import com.anotherglass.wine.Wine;

import com.anotherglass.postgres.DropAndCreateWinesTable;
import com.anotherglass.postgres.FetchWines;
import com.anotherglass.postgres.InsertWines;

import com.anotherglass.solr.DropIndex;
import com.anotherglass.solr.ImportWines;

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
	
	public void runDbImport() {		
		if(!running) {
			running = true;
			DropAndCreateWinesTable.create();									
			List<Wine> wines = new ArrayList<Wine>(FetchWines.fetch());
			if(!wines.isEmpty()) {
				InsertWines.insert(wines);	
			}			 
			running = false;
		}
	}	
	
	public void runSolrImport() {		
		if(!running) {
			running = true;
			ImportWines.run();
			running = false;
		}
	}
	
	public void dropSolr() {		
		if(!running) {
			running = true;
			DropIndex.drop();
			running = false;
		}
	}
}

