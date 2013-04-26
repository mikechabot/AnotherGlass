package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class CreateWinesTable {
	
	private static Logger log = Logger.getLogger(CreateWinesTable.class);
	
	public static boolean create() {
		
		boolean created = false;
				
		Connection connection = PostgresConnection.getPostgresConnection();
	    ResultSet resultSet = null;
	    
	    PreparedStatement drop = null;
	    PreparedStatement create = null;
	    Statement select = null;
	    
	    String dropSql = "drop table if exists wines";  
	    String createSql = "create table wines (ag_id serial, wine_id int, name varchar(32), description varchar(128), retail varchar(32), type varchar(32), url varchar(128), vintage varchar(32), priceMax varchar(10), priceMin varchar(10), priceRetail varchar(10))";
	    String selectSql = "select count(*) from wines";
		
		try {			
			drop = connection.prepareStatement(dropSql);
			create = connection.prepareStatement(createSql);
			
			drop.execute();
			create.execute();
			
			select = connection.createStatement();
			resultSet = select.executeQuery(selectSql);

            if (resultSet.next()) {
            	created = true;    
               	log.info("Table 'wines' created successfully!");
            }
            
            drop.close();
            create.close();
            connection.close();
            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
		
		return created;		
	}
}
