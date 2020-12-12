package com.esd.model.service;

import com.esd.model.dao.SystemSettingDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.SystemSetting;

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

    public boolean updateSystemSetting(SystemSetting systemSetting) throws SQLException {
        return systemSettingDao.getInstance().updateSetting(systemSetting);
    }

    public synchronized static SystemSettingService getInstance() {
        if (instance == null){
            instance = new SystemSettingService(SystemSettingDao.getInstance());
        }
        return instance;
    }
}