package com.esd.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static ConnectionManager instance;
    private Properties properties = new Properties();
    private Connection connection = null;

    private ConnectionManager() {
        try {
            InputStream in = ConnectionManager.class.getResourceAsStream("/database.conf");
            properties.load(in);
            initiliseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initiliseConnection(){
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://" + properties.getProperty("databaseURl") + ":" + properties.getProperty("port")+"/" + properties.getProperty("database"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public synchronized static ConnectionManager getInstance(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }
}
