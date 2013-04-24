package com.anotherglass.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.anotherglass.utils.Configuration.ConfigurationException;


public class PostgresConnection {
	
	private static Logger log = Logger.getLogger(PostgresConnection.class);
	
	public static Connection getPostgresConnection() {
		
		Configuration config = Configuration.getInstance();
		try {
			config.initialize();
		} 
		catch (ConfigurationException e) {
			log.error("could not load configuration for some reason", e);
		}

		String url = config.getRequiredString("postgresDbUrl");
		String user = config.getRequiredString("postgresDbUser");
		String pass = config.getRequiredString("postgresDbPass");		
		
		Connection connection = null;
	    Statement st = null;
	    ResultSet rs = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, pass);
            st = connection.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println("Found Version: " + rs.getString(1));
            }
			
			
			if(connection!=null) {
				log.info("Connection established.");
			}
		} catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver.", e);
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
		return connection;
	}

	public static void main (String[] args) {
		Connection connection = PostgresConnection.getPostgresConnection();
		if(connection!=null) {
			log.info("let's do stuff");
		}
	}
	
}
