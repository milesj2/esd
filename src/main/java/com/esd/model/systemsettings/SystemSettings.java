package com.esd.model.systemsettings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.esd.model.dao.ConnectionManager;

/**
 * Original author: Sam Barba
 * 
 * Use: static class which contains system settings for system-wide access
 * 
 */
public class SystemSettings {

	private static final String SETTINGS_PATH = "/systemsettings.conf";

	private Properties properties;

	private static SystemSettings instance;

	private SystemSettings() {
		try {
			InputStream in = ConnectionManager.class.getResourceAsStream(SETTINGS_PATH);
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	public void save() {
		try {
			properties.store(new FileOutputStream(SETTINGS_PATH), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static SystemSettings getInstance() {
		if (instance == null) {
			instance = new SystemSettings();
		}
		return instance;
	}
}