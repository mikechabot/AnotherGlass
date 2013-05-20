package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.anotherglass.utils.DbUtil;
import com.anotherglass.wine.Wine;

public class InsertWines {
	
	private static Logger log = Logger.getLogger(InsertWines.class);
	
	public void insert(List<Wine> wines) {
		
		PostgresConnection postgres= new PostgresConnection();
		Connection connection = postgres.getPostgresConnection();
	    
	    PreparedStatement insert = null;
	    String sql =  null;
	    	
	    log.info("Writing wines to database");
		try {			
			for(Wine temp : wines) {
				String id = DbUtil.out(Long.toString(temp.getId())); 	
				String name = DbUtil.out(temp.getName());
				String description = DbUtil.out(temp.getDescription());
				String retail = DbUtil.out(temp.getRetail());
				String type = DbUtil.out(temp.getType());
				String url = DbUtil.out(temp.getUrl());
				String vintage = DbUtil.out(temp.getVintage());
				String priceMax = DbUtil.out(temp.getPriceMax());
				String priceMin = DbUtil.out(temp.getPriceMin());
				String priceRetail = DbUtil.out(temp.getPriceRetail());
				
				sql = "insert into wines (wine_id, name, description, retail, type, url, vintage, priceMax, priceMin, priceRetail) values (" + id + "," + name + "," + description + "," 
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
			log.error("Failure inserting to 'wines' table", e);
		}
		log.info("Write complete");
	}

}
