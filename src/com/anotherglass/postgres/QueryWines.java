package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class QueryWines {
	
	private static Logger log = Logger.getLogger(QueryWines.class);
	
	public int query() {
		
		PostgresConnection postgres= new PostgresConnection();
		Connection connection = postgres.getPostgresConnection();
	    
		ResultSet resultSet = null;
	    Statement select = null;
	    
	    String sql = "select count(*) from wines";
		
	    int size = 0;
		try {				
			select = connection.createStatement();
			resultSet = select.executeQuery(sql);
            if (resultSet.next()) {
               	size = Integer.parseInt(resultSet.getString(1));
            	log.info("Table size ('wines'): " + size);
            }
            select.close();
            connection.close();            
		} catch (SQLException e) {
			log.error("Unable to perform query", e);
		}
		return size;
	}
}
