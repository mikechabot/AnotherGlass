package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DropAndCreateWinesTable {
	
	private static Logger log = Logger.getLogger(DropAndCreateWinesTable.class);
	
	public static void create() {
		
		Connection connection = new PostgresConnection().get();
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
}
