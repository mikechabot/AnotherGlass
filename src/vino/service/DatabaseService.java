package vino.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import vino.database.Database;
import vino.model.Appellation;
import vino.model.Wine;
import vino.utils.DbUtils;

public class DatabaseService {

	private static Logger log = Logger.getLogger(DatabaseService.class);
	
	public static void dropAndCreateWinesTable() {
		
		Connection connection = new Database().getConnection();

		PreparedStatement dropWines = null;
		PreparedStatement dropAppellations = null;
	    PreparedStatement createWines = null;
	    PreparedStatement createAppellations = null;
	    
	    // Lots'o'Sql
	    String dropWinesSql = "drop table if exists wines";
	    String dropAppellationsSql = "drop table if exists appellations";
	    String createWinesSql = "create table wines (ag_id serial unique, wine_id varchar(128), name varchar(256), description varchar(10000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max numeric, price_min numeric, price_retail numeric)";
	    String createAppellationsSql = "create table appellations (ag_id serial unique, wine_id varchar(128), appellation_id varchar(128), name varchar(256), url varchar(512), area varchar(256))";
		
		try {			
			dropWines = connection.prepareStatement(dropWinesSql);
			dropAppellations = connection.prepareStatement(dropAppellationsSql);
			createWines = connection.prepareStatement(createWinesSql);
			createAppellations = connection.prepareStatement(createAppellationsSql);
			
			dropWines.execute();
			dropAppellations.execute();
			if (createWines.execute()) log.info("Table 'wines' created");
			if (createAppellations.execute()) log.info("Table 'appellations' created");
						
			dropWines.close();
			dropAppellations.close();
            createWines.close();
            createAppellations.close();
            connection.close();
            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
	}
	
	public static void insertWines(List<Wine> wines) {
		
		Connection connection = new Database().getConnection();	    
	    PreparedStatement insertWine = null;
	    PreparedStatement insertAppellation = null;

	    String wineSql = "insert into wines (wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail) values (?,?,?,?,?,?,?,?,?,?);";
	    String appellationSql = "insert into appellations (appellation_id, wine_id, name, url, area) values (?,?,?,?,?);";
	    	
	    log.info("Writing wines to database");
		try {			
			for(Wine wine : wines) {
				insertWine = connection.prepareStatement(wineSql);
				insertAppellation = connection.prepareStatement(appellationSql);
				
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
				
				Appellation appellation = wine.getAppellation();
				if(appellation != null) {
					insertAppellation.setLong(1, appellation.getId());
					insertAppellation.setLong(2, wine.getId());
					insertAppellation.setString(3, appellation.getName());
					insertAppellation.setString(4, appellation.getUrl());
					insertAppellation.setString(5, appellation.getArea());
					insertAppellation.execute();
				} else {
					log.warn("Appellation was empty!");
				}
			}
			insertAppellation.close();
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
		
        String selectSql = "select w.wine_id, w.name, description, retail, type, w.url, vintage, price_max, price_min, price_retail, a.name, a.url from wines w, appellations a where lower(w.name) like lower(?) and w.wine_id = a.wine_id limit 50;";
        
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
        		wine.setDescription(rs.getString(3));
        		wine.setRetail(rs.getString(4));
        		wine.setType(rs.getString(5));
        		wine.setUrl(rs.getString(6));
        		wine.setVintage(rs.getString(7));
        		wine.setPriceMax(rs.getLong(8));
        		wine.setPriceMin(rs.getLong(9));
        		wine.setPriceRetail(rs.getLong(10));
        		
        		Appellation appellation = new Appellation();
        		appellation.setName(rs.getString(11));
        		appellation.setUrl(rs.getString(12));
        		
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
