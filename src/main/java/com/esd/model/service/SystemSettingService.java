package com.esd.model.service;

import com.esd.model.dao.SystemSettingDao;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is to do any functionality needed for system settings
 */
public class SystemSettingService {

    private static SystemSettingService instance;
    private SystemSettingDao systemSettingDao;

    private SystemSettingService(SystemSettingDao systemSettingDao){
        if (systemSettingDao == null){
            throw new IllegalArgumentException("systemSettingDao must not be null");
        }
        this.systemSettingDao = systemSettingDao;
    }

    public int getIntegerSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getIntegerSettingValueByKey(settingKey);
    }

    public double getDoubleSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getDoubleSettingValueByKey(settingKey);
    }

    public String getSettingValueByKey(String settingKey) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getSettingValueByKey(settingKey);
    }

    public boolean updateSystemSetting(String settingKey, String settingValue) throws SQLException {
        return systemSettingDao.getInstance().updateSetting(settingKey, settingValue);
    }

    public synchronized static SystemSettingService getInstance() {
        if (instance == null){
            instance = new SystemSettingService(SystemSettingDao.getInstance());
        }
        return instance;
    }
}
