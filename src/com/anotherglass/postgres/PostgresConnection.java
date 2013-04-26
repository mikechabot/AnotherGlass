package com.anotherglass.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.anotherglass.misc.Configuration;
import com.anotherglass.misc.Configuration.ConfigurationException;


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
	    Statement statement = null;
	    ResultSet resultSet = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, pass);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT VERSION()");
            if (resultSet.next()) {
                log.info("Found Version: " + resultSet.getString(1));
            }						
		} catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver.", e);
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server.", e);
		}
		return connection;
	}
	
}