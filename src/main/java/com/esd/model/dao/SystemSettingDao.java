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

    private static final String GET_SYSTEMSETTING = "SELECT settingValue FROM systemSetting WHERE settingKey=?";
    private static final String UPDATE_SYSTEMSETTING = "UPDATE systemSetting SET settingValue=? WHERE settingKey=?";
    private static SystemSettingDao instance;

    private SystemSettingDao() {
    }

    /**
     * Get an Integer system setting by its key
     */
    public int getIntegerSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, settingKey);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key '" + settingKey + "'");
        }

        return result.getInt(settingKey);
    }

    /**
     * Get a Double system setting by its key
     */
    public double getDoubleSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, settingKey);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key '" + settingKey + "'");
        }

        return result.getDouble(settingKey);
    }

    /**
     * Get a String system setting by its key
     */
    public String getSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_SYSTEMSETTING);

        statement.setString(1, settingKey);

        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if (!resultFound) {
            throw new InvalidIdValueException("No system setting key '" + settingKey + "'");
        }

        return result.getString(settingKey);
    }

    /**
     * Update a system setting
     */
    public boolean updateSetting(String settingKey, String settingValue) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_SYSTEMSETTING);

        statement.setString(1, settingValue);
        statement.setString(2, settingKey);

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
