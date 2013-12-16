package vino.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import vino.config.Configuration;
import vino.config.Configuration.DatabaseConfiguration;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
	
	public Connection getConnection() {
		
		Configuration config = Configuration.getInstance();
		DatabaseConfiguration databaseConfiguration = config.getDatabase();
		
		Connection connection = null;
		
		try {
			Class.forName(databaseConfiguration.getDriver());
			connection = DriverManager.getConnection(databaseConfiguration.getUrl(), databaseConfiguration.getUsername(), databaseConfiguration.getPassword());				
		} catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver", e);
		} catch (SQLException e) {
			log.error("Unable to connect to Postgres server", e);
		}
		return connection;
	}	
}