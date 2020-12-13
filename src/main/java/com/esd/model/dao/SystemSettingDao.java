package com.esd.model.dao;

import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is all DAO operations in relation to system settings
 */
public class SystemSettingDao {

    private static final String GET_SYSTEMSETTING = "SELECT value FROM systemsetting WHERE settingKey=?";
    private static final String UPDATE_SYSTEMSETTING = "UPDATE systemsetting SET value=? WHERE settingKey=?";
    private static SystemSettingDao instance;

    private SystemSettingDao() {
    }

    /**
     * Get an Integer system setting by its key
     */
    public int getIntegerSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, key);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key " + key);
        }

        return result.getInt(key);
    }

    /**
     * Get a Double system setting by its key
     */
    public double getDoubleSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, key);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key " + key);
        }

        return result.getDouble(key);
    }

    /**
     * Get a String system setting by its key
     */
    public String getSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, key);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key " + key);
        }

        return result.getString(key);
    }

    /**
     * Update a system setting
     */
    public boolean updateSetting(String key, String value) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_SYSTEMSETTING);

        statement.setString(1, value);
        statement.setString(2, key);

        int result = statement.executeUpdate();

        if (result == 1){
            return true;
        } else if (result == 0){
            throw new SQLException();
        } else {
            return false;
        }
    }

    public synchronized static SystemSettingDao getInstance(){
        if(instance == null){
            instance = new SystemSettingDao();
        }
        return instance;
    }
}
