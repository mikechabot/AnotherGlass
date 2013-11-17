package com.anotherglass.vino.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class Query {
	
	private static Logger log = Logger.getLogger(Query.class);
	
	Connection connection = new Database().get();	 
	Statement select = null;
	
	public ResultSet execute(String sql) {		
		ResultSet resultSet = null;	    			   
		try {				
			select = connection.createStatement();
			resultSet = select.executeQuery(sql);
		} catch (SQLException e) {
			log.error("Unable to perform query", e);
		}
		return resultSet;
	}
	
	public void close() throws SQLException {
		select.close();
		connection.close();
	}
}
