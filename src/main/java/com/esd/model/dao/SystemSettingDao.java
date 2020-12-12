package com.esd.model.dao;

import com.esd.model.data.persisted.SystemSetting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is all DAO operations in relation to system settings
 */
public class SystemSettingDao {

    private static final String UPDATE_SYSTEMSETTING = "UPDATE SYSTEMSETTING SET value=? WHERE settingKey=?";
    private static SystemSettingDao instance;

    private SystemSettingDao() {
    }

    /**
     * Update a system setting
     */
    public boolean updateSetting(SystemSetting systemSetting) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_SYSTEMSETTING);

        statement.setString(1,systemSetting.getValue());
        statement.setString(2,systemSetting.getKey());

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
