package vino.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.javalite.activejdbc.Base;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import vino.Configuration;
import vino.Configuration.DatabaseConfiguration;

public class Database {
	
	private static Logger log = Logger.getLogger(Database.class);
		
	private static ComboPooledDataSource dataSource;
	
	static {
		Configuration config = Configuration.getInstance();
		DatabaseConfiguration databaseConfiguration = config.getDatabase();
		
		try {	
			System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();
            ic.createSubcontext("jdbc");
            
			dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(databaseConfiguration.getDriver());
			dataSource.setJdbcUrl(databaseConfiguration.getUrl());
			dataSource.setUser(databaseConfiguration.getUsername());
			dataSource.setPassword(databaseConfiguration.getPassword());
			
			ic.bind("jdbc/datasource", dataSource);
		}
		catch (Exception e) {
			log.error("Unable to initalize datasource", e);
		}
	} 
	
	public Database() {}
		
	public void open() throws SQLException {
		Base.open("jdbc/datasource");
	}
	
	public Statement getStatement() throws SQLException {
		return Base.connection().createStatement();
	}
	
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		return Base.connection().prepareStatement(sql);
	}
	
	public void close() {
		Base.close();
	}
	
}