package com.esd.model.data.persisted;

import com.esd.model.data.UserGroup;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about a user
 */
public class User {

    private int id;
    private String username;
    private String password;
    private UserGroup userGroup;
    private UserDetails userDetails;
    private boolean active = false;

    public User() {
    }

    public User(String username, String password, UserGroup userGroup, boolean active) {
        this.username = username;
        this.password = password;
        this.userGroup = userGroup;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
