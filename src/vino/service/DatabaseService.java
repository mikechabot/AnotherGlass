package vino.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.Query;
import vino.database.Database;
import vino.model.Appellation;
import vino.model.Region;
import vino.model.SearchResult;
import vino.model.Vineyard;
import vino.model.Wine;

public class DatabaseService {

	private static Logger log = Logger.getLogger(DatabaseService.class);
	
	public static void dropSchema() {
		Connection connection = new Database().getConnection();
		
		try {			
			PreparedStatement dropWines = connection.prepareStatement("drop table if exists wines");
			PreparedStatement dropRegions = connection.prepareStatement("drop table if exists regions");
			PreparedStatement dropVineyards =  connection.prepareStatement("drop table if exists vineyards");
			PreparedStatement dropAppellations =connection.prepareStatement("drop table if exists appellations");
			
			dropWines.execute();
			dropRegions.execute();
			dropVineyards.execute();
			dropAppellations.execute();
			
			dropWines.close();
			dropRegions.close();
			dropVineyards.close();
			dropAppellations.close();
			connection.close();			
		} catch (SQLException e) {
			log.error("Error while dropping schema", e);
		}
	}
	
	public static void createSchema() {
		Connection connection = new Database().getConnection();
		
	    PreparedStatement createWines = null;
	    PreparedStatement createRegions = null;
	    PreparedStatement createVineyards = null;
	    PreparedStatement createAppellations = null;
	    
	    String createWinesSql = "create table wines (ag_id serial unique, wine_id bigint, name varchar(256), description varchar(5000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max numeric, price_min numeric, price_retail numeric, region_id bigint, appellation_id bigint, vineyard_id bigint)";
	    String createRegionsSql = "create table regions (ag_id serial unique, region_id bigint unique, name varchar(256), url varchar(512), area varchar(256))";
	    String createVineyardsSql = "create table vineyards (ag_id serial unique, vineyard_id bigint unique, name varchar(256), url varchar(512), imageUrl varchar(512), appellation_id bigint)";
	    String createAppellationsSql = "create table appellations (ag_id serial unique, appellation_id bigint unique, name varchar(256), url varchar(512), region_id bigint)";
	    		
		try {
			createWines = connection.prepareStatement(createWinesSql);
			createRegions = connection.prepareStatement(createRegionsSql);
			createAppellations = connection.prepareStatement(createAppellationsSql);
			createVineyards = connection.prepareStatement(createVineyardsSql);

			if (createWines.execute()) log.info("Table 'wines' created");
			if (createRegions.execute()) log.info("Table 'regions' created");
			if (createVineyards.execute()) log.info("Table 'vineyards' created");
			if (createAppellations.execute()) log.info("Table 'appellations' created");
						
            createWines.close();
            createRegions.close();
            createVineyards.close();
            createAppellations.close();            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
	}
	
	public static void insertWines(List<Wine> wines) {
		Connection connection = new Database().getConnection();
		
	    PreparedStatement insertWine = null;
	    PreparedStatement insertRegion = null;
	    PreparedStatement insertVineyard = null;
	    PreparedStatement insertAppellation = null;
	    PreparedStatement updateRegion = null;
	    PreparedStatement updateVineyard = null;
	    PreparedStatement updateAppellation = null;

	    String insertWineSql = "insert into wines (wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail, region_id, appellation_id, vineyard_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?);";
	    String insertRegionSql = "insert into regions (region_id, name, url, area) values (?,?,?,?);";
	    String insertVineyardSql = "insert into vineyards (vineyard_id, name, url, imageUrl, appellation_id) values (?,?,?,?,?);";
	    String insertAppellationSql = "insert into appellations (appellation_id, name, url, region_id) values (?,?,?,?);";
	    
	    String updateRegionSql = "update regions set region_id = ? where region_id = ?;";
	    String updateVineyardSql = "update vineyards set vineyard_id = ? where vineyard_id = ?;";
	    String updateAppellationSql = "update appellations set appellation_id = ? where appellation_id = ?;";
	    	
	    log.info("Writing wines to database");
		try {			
			for (Wine wine : wines) {
				
				insertWine = connection.prepareStatement(insertWineSql);
				insertRegion = connection.prepareStatement(insertRegionSql);
				insertVineyard = connection.prepareStatement(insertVineyardSql);
				insertAppellation = connection.prepareStatement(insertAppellationSql);
				
				updateRegion = connection.prepareStatement(updateRegionSql);
				updateVineyard = connection.prepareStatement(updateVineyardSql);
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
						
						// Set regiond_id for parent tables
						insertWine.setLong(11, region.getId());
						insertAppellation.setLong(4, region.getId());
						
						// Postgres doesn't support INSERT IGNORE, so attempt an UPDATE, then an INSERT 
						updateRegion.execute();
						if (updateRegion.getUpdateCount() == 0) {
							insertRegion.execute();
						}
					} else {
						// If there's no region, set a flag, probably should just change this to 0?
						insertWine.setLong(11, -1);
						insertAppellation.setLong(4, -1);
						log.warn("Region was empty - wine_id: " + wine.getId());
					}
					updateAppellation.setLong(1, appellation.getId());
					updateAppellation.setLong(2, appellation.getId());
					
					insertAppellation.setLong(1, appellation.getId());
					insertAppellation.setString(2, appellation.getName());
					insertAppellation.setString(3, appellation.getUrl());
					
					// Set appellation_id for parent tables
					insertWine.setLong(12, appellation.getId());
					insertVineyard.setLong(5, appellation.getId());
					
					// Postgres doesn't support INSERT IGNORE, so attempt an UPDATE, then an INSERT 
					updateAppellation.execute();
					if (updateAppellation.getUpdateCount() == 0) {
						insertAppellation.execute();
					}
				} else {
					// If there's no appellation, set a flag, probably should just change this to 0?
					insertWine.setLong(11, -1);
					insertWine.setLong(12, -1);
					insertVineyard.setLong(5, -1);
					log.warn("Appellation was empty - wine_id: " + wine.getId());
				}
				
				Vineyard vineyard = wine.getVineyard();
				if (vineyard != null) {
					updateVineyard.setLong(1, vineyard.getId());
					updateVineyard.setLong(2, vineyard.getId());
					
					insertVineyard.setLong(1, vineyard.getId());
					insertVineyard.setString(2, vineyard.getName());
					insertVineyard.setString(3, vineyard.getUrl());
					insertVineyard.setString(4, vineyard.getImageUrl());
					
					// Set appellation_id for parent table
					insertWine.setLong(13, vineyard.getId());
					
					// Postgres doesn't support INSERT IGNORE, so attempt an UPDATE, then an INSERT 
					updateVineyard.execute();
					if (updateVineyard.getUpdateCount() == 0) {
						insertVineyard.execute();
					}
				} else {
					insertWine.setLong(13, -1);
					log.warn("Vineyard was empty - wine_id: " + wine.getId());
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
			log.error("Error while inserting wines", e);
		}
		log.info("Write complete");
	}
	
	public static List<SearchResult> searchWines(Query query) {	
		Connection connection = new Database().getConnection();
		PreparedStatement select = null;
		ResultSet rs = null;
		
		List<SearchResult> wines = new ArrayList<SearchResult>();
		
        String selectSql = "select wine_id, w.name, w.url, a.appellation_id, a.name, a.url, r.region_id, r.name, r.url, v.vineyard_id, v.name, v.url from wines w left outer join appellations a on (w.appellation_id = a.appellation_id) left outer join regions r on (w.region_id = r.region_id) left outer join vineyards v on (w.vineyard_id = v.vineyard_id) where lower(w.name) like lower(?) limit 50;";
        
        try {
        	select = connection.prepareStatement(selectSql);        	
        	select.setString(1, "%"+query.getQuery()+"%");
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
        		
        		Vineyard vineyard = new Vineyard();
        		vineyard.setId(rs.getLong(10));
        		vineyard.setName(rs.getString(11));
        		vineyard.setUrl(rs.getString(12));
        		
        		appellation.setRegion(region);
        		wine.setAppellation(appellation);
        		wine.setVineyard(vineyard);
        		
        		wines.add(wine);
            }
            
            if (select != null) select.close();
            if (connection != null) connection.close(); 
            
    	} catch (SQLException e) {
			log.error("Error while 'wines' table", e);
		}
        log.debug("Retrieved " + wines.size() + " wines from the db");
        return wines;
	}
	
	public static List<SearchResult> searchVineyards(Query query) {		
		Connection connection = new Database().getConnection();
		PreparedStatement select = null;
		ResultSet rs = null;
		
		List<SearchResult> vineyards = new ArrayList<SearchResult>();
		
        String selectSql = "select v.vineyard_id, v.name, v.url, a.appellation_id, a.name, a.url from vineyards v left outer join appellations a on (v.appellation_id = a.appellation_id) where lower(v.name) like lower(?) limit 50;";
        try {
        	select = connection.prepareStatement(selectSql);        	
        	select.setString(1, "%"+query.getQuery()+"%");
        	log.trace(select);
            rs = select.executeQuery();
            while (rs.next()) {
        		Vineyard vineyard = new Vineyard();
        		vineyard.setId(rs.getLong(1));
        		vineyard.setName(rs.getString(2));
        		vineyard.setUrl(rs.getString(3));
        		
        		Appellation appellation = new Appellation();
        		appellation.setId(rs.getLong(4));
        		appellation.setName(rs.getString(5));
        		appellation.setUrl(rs.getString(6));
        		
        		vineyard.setAppellation(appellation);
        		vineyards.add(vineyard);
            }
            
            if (select != null) select.close();
            if (connection != null) connection.close(); 
            
    	} catch (SQLException e) {
			log.error("Error while 'vineyards' table", e);
		}
        log.debug("Retrieved " + vineyards.size() + " vineyards from the db");
        return vineyards;
	}
	
	public static List<SearchResult> searchRegions(Query query) {
		Connection connection = new Database().getConnection();
		PreparedStatement select = null;
		ResultSet rs = null;
		
		List<SearchResult> regions = new ArrayList<SearchResult>();
		
        String selectSql = "select a.appellation_id, a.name, a.url, count(w.ag_id), r.region_id, r.name, r.url from appellations a left outer join regions r on (a.region_id = r.region_id), wines w where w.appellation_id = a.appellation_id and (lower(a.name) like lower(?) or lower(a.name) like lower(?)) group by a.appellation_id, a.name, a.url, r.region_id, r.name, r.url limit 50;";
        try {
        	select = connection.prepareStatement(selectSql);        	
        	select.setString(1, "%"+query.getQuery()+"%");
        	select.setString(2, "%"+query.getQuery()+"%");
        	log.trace(select);
            rs = select.executeQuery();
            while (rs.next()) {
            	Appellation appellation = new Appellation();
            	appellation.setId(rs.getLong(1));
        		appellation.setName(rs.getString(2));
        		appellation.setUrl(rs.getString(3));
        		appellation.setWineCount(rs.getLong(4));
        		
        		Region region = new Region();
        		region.setId(rs.getLong(5));
        		region.setName(rs.getString(6));
        		region.setUrl(rs.getString(7));
        		
        		appellation.setRegion(region);
        		regions.add(appellation);
            }
            
            if (select != null) select.close();
            if (connection != null) connection.close(); 
            
    	} catch (SQLException e) {
			log.error("Error while 'appellations/regions' table", e);
		}
        log.debug("Retrieved " + regions.size() + " regions from the db");
        return regions;
	}
	
}
