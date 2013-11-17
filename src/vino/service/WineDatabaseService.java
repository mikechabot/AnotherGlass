package vino.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import vino.database.Database;
import vino.database.Utils;
import vino.model.Wine;

public class WineDatabaseService {

	private static Logger log = Logger.getLogger(WineDatabaseService.class);
	
	public static void dropAndCreateWinesTable() {
		
		Connection connection = new Database().get();
	    ResultSet resultSet = null;
	    
	    PreparedStatement drop = null;
	    PreparedStatement create = null;
	    Statement select = null;
	    
	    String dropSql = "drop table if exists wines";  
	    String createSql = "create table wines (ag_id serial unique, wine_id varchar(128), name varchar(256), description varchar(10000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max varchar(10), price_min varchar(10), price_retail varchar(10))";
	    String selectSql = "select count(*) from wines";
		
		try {			
			drop = connection.prepareStatement(dropSql);
			create = connection.prepareStatement(createSql);
			
			drop.execute();
			create.execute();
			
			select = connection.createStatement();
			resultSet = select.executeQuery(selectSql);			

            if (resultSet.next()) {
               	log.info("Table 'wines' created");
            }
            
            drop.close();
            create.close();
            connection.close();
            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
	}
	
	public static void insertWines(List<Wine> wines) {
		
		Connection connection = new Database().get();
	    
	    PreparedStatement insert = null;
	    String sql =  null;
	    	
	    log.info("Writing wines to database");
		try {			
			for(Wine temp : wines) {
				String id = Utils.sanitizeSingleQuote(Long.toString(temp.getId())); 	
				String name = Utils.sanitizeSingleQuote(temp.getName());
				String description = Utils.sanitizeSingleQuote(temp.getDescription());
				String retail = Utils.sanitizeSingleQuote(temp.getRetail());
				String type = Utils.sanitizeSingleQuote(temp.getType());
				String url = Utils.sanitizeSingleQuote(temp.getUrl());
				String vintage = Utils.sanitizeSingleQuote(temp.getVintage());
				String priceMax = Utils.sanitizeSingleQuote(temp.getPriceMax());
				String priceMin = Utils.sanitizeSingleQuote(temp.getPriceMin());
				String priceRetail = Utils.sanitizeSingleQuote(temp.getPriceRetail());
				
				sql = "insert into wines (wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail) values (" + id + "," + name + "," + description + "," 
                        + retail + "," + type + "," + url + "," 
                        + vintage + "," + priceMax + "," + priceMin + ","
                        + priceRetail + ");";
				
				log.debug("sql=" + sql);
				insert = connection.prepareStatement(sql);			
				insert.execute();	
			}          
			insert.close();
            connection.close();            
		} catch (SQLException e) {
			log.error("Failure inserting into 'wines' table", e);
		}
		log.info("Write complete");
	}
	
	public static List<Wine> fetchWines() {
		return null;
	}
	
	public static Wine fetchWine(String wine) {
		return null;
	}
}
