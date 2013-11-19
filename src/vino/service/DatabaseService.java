package vino.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vino.database.Database;
import vino.model.Wine;
import vino.utils.DbUtils;

public class DatabaseService {

	private static Logger log = Logger.getLogger(DatabaseService.class);
	
	public static void dropAndCreateWinesTable() {
		
		Connection connection = new Database().getConnection();
	    ResultSet resultSet = null;	    
	    PreparedStatement drop = null;
	    PreparedStatement create = null;
	    Statement select = null;
	    
	    String dropSql = "drop table if exists wines";  
	    String createSql = "create table wines (ag_id serial unique, wine_id varchar(128), name varchar(256), description varchar(10000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max numeric, price_min numeric, price_retail numeric)";
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
		
		Connection connection = new Database().getConnection();	    
	    PreparedStatement insert = null;

	    String sql = "insert into wines (wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail) values (?,?,?,?,?,?,?,?,?,?);";
	    	
	    log.info("Writing wines to database");
		try {			
			for(Wine wine : wines) {
				insert = connection.prepareStatement(sql);
				insert.setLong(1, wine.getId());
				insert.setString(2, wine.getName());
				insert.setString(3, wine.getDescription());
				insert.setString(4, wine.getRetail());
				insert.setString(5, wine.getType());
				insert.setString(6, wine.getUrl());
				insert.setString(7, wine.getVintage());
				insert.setDouble(8, wine.getPriceMax());
				insert.setDouble(9, wine.getPriceMin());
				insert.setDouble(10, wine.getPriceRetail());		
				insert.execute();	
			}          
			insert.close();
            connection.close();            
		} catch (SQLException e) {
			log.error("Failure inserting into 'wines' table", e);
		}
		log.info("Write complete");
	}
	
	public List<Wine> searcWines(String query) {
		
		Connection connection = new Database().getConnection();
		PreparedStatement select = null;
		ResultSet rs = null;
		
        String selectSql = "select wine_id, name, description, retail, type, url, vintage, price_max, price_min, price_retail from wines limit 50 where name like '%?%'"; 
 
        List<Wine> wines = new ArrayList<Wine>();
        try {
        	select = connection.prepareStatement(selectSql);
        	select.setString(1, query);
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
            		wines.add(wine);
            }
            
            if (select != null) select.close();
            if (connection != null) connection.close(); 
            
    	} catch (SQLException e) {
			log.error("Failure inserting into 'wines' table", e);
		}
        
        return wines;
	}
	
}
