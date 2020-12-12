package com.esd.model.data.persisted;

import com.esd.model.data.UserGroup;

/**
 * Original Author: Sam Barba
 * Use: This class is a simple data class used to store the data about a system setting
 */
public class SystemSetting {

    private String key;
    private String value;

    public SystemSetting() {
    }

    public SystemSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
