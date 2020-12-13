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

    public int getIntegerSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getIntegerSettingValueByKey(key);
    }

    public double getDoubleSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getDoubleSettingValueByKey(key);
    }

    public String getSettingValueByKey(String key) throws SQLException, InvalidIdValueException {
        return systemSettingDao.getInstance().getSettingValueByKey(key);
    }

    public boolean updateSystemSetting(String key, String value) throws SQLException {
        return systemSettingDao.getInstance().updateSetting(key, value);
    }

    public synchronized static SystemSettingService getInstance() {
        if (instance == null){
            instance = new SystemSettingService(SystemSettingDao.getInstance());
        }
        return instance;
    }
}
