package com.esd.model.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Original Author: Jordan Hellier Use: This class is a singleton, The use of this class is to manage the connection to
 * the database
 */
public class ConnectionManager {

	private static ConnectionManager instance;

	private Properties properties = new Properties();

	private Connection connection = null;

	private ConnectionManager() {
		try {
			InputStream in = ConnectionManager.class.getResourceAsStream("/database.conf");
			properties.load(in);
			initialiseConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialiseConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			connection = DriverManager.getConnection(createConnectionURL(), createConnectionProperties());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private Properties createConnectionProperties() {
		Properties connectionProperties = new Properties();
		if (!properties.getProperty("user").equals("")) {
			connectionProperties.setProperty("user", properties.getProperty("user"));
		}
		if (!properties.getProperty("password").equals("")) {
			connectionProperties.setProperty("password", properties.getProperty("password"));
		}
		return connectionProperties;
	}

	private String createConnectionURL() {
		return String.format("jdbc:derby://%s:%s/%s", properties.getProperty("databaseURl"),
				properties.getProperty("port"), properties.getProperty("database"));
	}

	public Connection getConnection() {
		return connection;
	}

	public synchronized static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}
}
