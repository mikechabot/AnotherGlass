package vino.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import vino.config.Configuration;
import vino.config.Configuration.DatabaseConfiguration;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
	
	private DatabaseConfiguration databaseConfiguration;
	private Connection connection;
	
	public Database() {
		Configuration config = Configuration.getInstance();
		databaseConfiguration = config.getDatabase();

		try {
			Class.forName(databaseConfiguration.getDriver());
		}
		catch (ClassNotFoundException e) {
			log.error("Unable to locate Postgres driver", e);
		}
	}
	
	public void open() throws SQLException {
		if (connection != null) throw new SQLException("A connection has already been opened!!!");
		connection = DriverManager.getConnection(databaseConfiguration.getUrl(), databaseConfiguration.getUsername(), databaseConfiguration.getPassword());		
	}
	
	public Statement getStatement() throws SQLException {
		if (connection == null) throw new SQLException("A connection has not been opened!!!");
		return connection.createStatement();
	}
	
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		if (connection == null) throw new SQLException("A connection has not been opened!!!");
		return connection.prepareStatement(sql);
	}
	
	public void close() {
		if (connection !=  null) {
			try {				
				connection.close();
			}
			catch (SQLException e) {
				// do nothing
			}
		}
	}
	
}