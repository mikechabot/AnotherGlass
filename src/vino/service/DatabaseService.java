package vino.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.database.Database;
import vino.model.Appellation;
import vino.model.Region;
import vino.model.Wine;

public class DatabaseService {

	private static Logger log = Logger.getLogger(DatabaseService.class);
	
	public static void dropAndCreateWinesTable() {
		
		Connection connection = new Database().getConnection();

		PreparedStatement dropWines = null;
		PreparedStatement dropAppellations = null;
		PreparedStatement dropRegions = null;
	    PreparedStatement createWines = null;
	    PreparedStatement createRegions = null;
	    PreparedStatement createAppellations = null;
	    
	    // Lots'o'Sql
	    String dropWinesSql = "drop table if exists wines";
	    String dropRegionsSql = "drop table if exists regions";
	    String dropAppellationsSql = "drop table if exists appellations";
	    String createWinesSql = "create table wines (ag_id serial unique, wine_id bigint, name varchar(256), description varchar(5000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max numeric, price_min numeric, price_retail numeric, region_id bigint, appellation_id bigint)";
	    String createRegionsSql = "create table regions (ag_id serial unique, region_id bigint unique, name varchar(256), url varchar(512), area varchar(256))";
	    String createAppellationsSql = "create table appellations (ag_id serial unique, appellation_id bigint unique, name varchar(256), url varchar(512), region_id bigint)";
		
		try {			
			dropWines = connection.prepareStatement(dropWinesSql);
			dropAppellations = connection.prepareStatement(dropAppellationsSql);
			dropRegions = connection.prepareStatement(dropRegionsSql);
			createWines = connection.prepareStatement(createWinesSql);
			createRegions = connection.prepareStatement(createRegionsSql);
			createAppellations = connection.prepareStatement(createAppellationsSql);
			
			dropWines.execute();
			dropRegions.execute();
			dropAppellations.execute();
			if (createWines.execute()) log.info("Table 'wines' created");
			if (createRegions.execute()) log.info("Table 'regions' created");
			if (createAppellations.execute()) log.info("Table 'appellations' created");
						
			dropWines.close();
			dropRegions.close();
			dropAppellations.close();
            createWines.close();
            createRegions.close();
            createAppellations.close();
            connection.close();
            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
	}
	
	public static void insertWines(List<Wine> wines) {
		
		Connection connection = new Database().getConnection();	    
	    PreparedStatement insertWine = null;
	    PreparedStatement insertRegion = null;
	    PreparedStatement updateRegion = null;
	    PreparedStatement insertAppellation = null;
	    PreparedStatement updateAppellation = null;

	    String insertWineSql = "insert into wines (wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail, region_id, appellation_id) values (?,?,?,?,?,?,?,?,?,?,?,?);";
	    String insertAppellationSql = "insert into appellations (appellation_id, name, url, region_id) values (?,?,?,?);";
	    String insertRegionSql = "insert into regions (region_id, name, url, area) values (?,?,?,?);";
	    
	    String updateRegionSql = "update regions set region_id = ? where region_id = ?;";
	    String updateAppellationSql = "update appellations set appellation_id = ? where appellation_id = ?;";
	    	
	    log.info("Writing wines to database");
		try {			
			for (Wine wine : wines) {
				
				insertWine = connection.prepareStatement(insertWineSql);
				insertRegion = connection.prepareStatement(insertRegionSql);
				insertAppellation = connection.prepareStatement(insertAppellationSql);
				
				updateRegion = connection.prepareStatement(updateRegionSql);
				updateAppellation = connection.prepareStatement(updateAppellationSql);
				
				Appellation appellation = wine.getAppellation();
				if (appellation != null) {
					Region region = appellation.getRegion();
					if (region != null) {
						
						updateRegion.setLong(1, region.getId());
						updateRegion.setLong(2, region.getId());

						insertRegion.setLong(1, region.getId());
						insertRegion.setString(2, region.getName());
						insertRegion.setString(3, region.getUrl());
						insertRegion.setString(4, region.getArea());
						
						// Set regiond_id for the parent tables
						insertWine.setLong(11, region.getId());
						insertAppellation.setLong(4, region.getId());
						
						// Postgres doesn't support INSERT IGNORE, so attempt an UPDATE, then an INSERT 
						updateRegion.execute();
						if (updateRegion.getUpdateCount() == 0) {
							insertRegion.execute();
						}
					} else {
						// If there's no region, set a flag, probably should just change this to 0 
						insertWine.setLong(11, -1);
						insertAppellation.setLong(4, -1);
						log.warn("Region was empty - wine_id: " + wine.getId());
					}
					updateAppellation.setLong(1, appellation.getId());
					updateAppellation.setLong(2, appellation.getId());
					
					insertAppellation.setLong(1, appellation.getId());
					insertAppellation.setString(2, appellation.getName());
					insertAppellation.setString(3, appellation.getUrl());
					
					// Set appellation_id for parent table
					insertWine.setLong(12, appellation.getId());
					
					// Postgres doesn't support INSERT IGNORE, so attempt an UPDATE, then an INSERT 
					updateAppellation.execute();
					if (updateAppellation.getUpdateCount() == 0) {
						insertAppellation.execute();
					}
				} else {
					// If there's no appellation, set a flag, probably should just change this to 0
					insertWine.setLong(11, -1);
					insertWine.setLong(12, -1);
					log.warn("Appellation was empty - wine_id: " + wine.getId());
				}		
				
				// Prepare the insert statement
				insertWine.setLong(1, wine.getId());
				insertWine.setString(2, wine.getName());
				insertWine.setString(3, wine.getDescription());
				insertWine.setString(4, wine.getRetail());
				insertWine.setString(5, wine.getType());
				insertWine.setString(6, wine.getUrl());
				insertWine.setString(7, wine.getVintage());
				insertWine.setDouble(8, wine.getPriceMax());
				insertWine.setDouble(9, wine.getPriceMin());
				insertWine.setDouble(10, wine.getPriceRetail());
				insertWine.execute();
			}			
			// Close statements and connection
			insertAppellation.close();
			updateAppellation.close();
			insertRegion.close();
			updateRegion.close();
			insertWine.close();
            connection.close();            
		} catch (SQLException e) {
			log.error("Failure transacting with database", e);
		}
		log.info("Write complete");
	}
	
	public static List<Wine> searchWines(String query) {
		
		Connection connection = new Database().getConnection();
		PreparedStatement select = null;
		ResultSet rs = null;
		
        String selectSql = "select wine_id, w.name, w.url, a.appellation_id, a.name, a.url, r.region_id, r.name, r.url from wines w left outer join appellations a on (w.appellation_id = a.appellation_id) left outer join regions r on (w.region_id = r.region_id) where lower(w.name) like lower(?) limit 50;";
        
        List<Wine> wines = new ArrayList<Wine>();
        try {
        	select = connection.prepareStatement(selectSql);
        	
        	select.setString(1, "%"+query+"%");
        	log.trace(select);
            rs = select.executeQuery();
            while (rs.next()) {
            	Wine wine = new Wine();
        		wine.setId(rs.getLong(1));
        		wine.setName(rs.getString(2));
        		wine.setUrl(rs.getString(3));
        		
        		Appellation appellation = new Appellation();
        		appellation.setId(rs.getLong(4));
        		appellation.setName(rs.getString(5));
        		appellation.setUrl(rs.getString(6));
        		
        		Region region = new Region();
        		region.setId(rs.getLong(7));
        		region.setName(rs.getString(8));
        		region.setUrl(rs.getString(9));
        		
        		appellation.setRegion(region);
        		wine.setAppellation(appellation);
        		wines.add(wine);
            }
            
            if (select != null) select.close();
            if (connection != null) connection.close(); 
            
    	} catch (SQLException e) {
			log.error("Failure searching 'wines' table", e);
		}
        log.debug("Retrieved " + wines.size() + " wines from the db");
        return wines;
	}
	
}
