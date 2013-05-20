package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class CreateWinesTable {
	
	private static Logger log = Logger.getLogger(CreateWinesTable.class);
	
	public void create() {
		
		PostgresConnection postgres= new PostgresConnection();
		Connection connection = postgres.getPostgresConnection();
	    ResultSet resultSet = null;
	    
	    PreparedStatement drop = null;
	    PreparedStatement create = null;
	    Statement select = null;
	    
	    String dropSql = "drop table if exists wines";  
	    String createSql = "create table wines (ag_id serial unique, wine_id varchar(128), name varchar(256), description varchar(10000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), priceMax varchar(10), priceMin varchar(10), priceRetail varchar(10))";
	    String selectSql = "select count(*) from wines";
		
		try {			
			drop = connection.prepareStatement(dropSql);
			create = connection.prepareStatement(createSql);
			
			drop.execute();
			create.execute();
			
			select = connection.createStatement();
			resultSet = select.executeQuery(selectSql);			

            if (resultSet.next()) {
               	log.info("Table 'wines' created successfully!");
            }
            
            drop.close();
            create.close();
            connection.close();
            
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
	}
}
