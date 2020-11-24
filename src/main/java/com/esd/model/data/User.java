package com.esd.model.data;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about a user
 */
public class User {

    private String username;
    private String password;
    private UserGroup userGroup;

    public User() {
    }

    public User(String username, String password, UserGroup userGroup) {
        this.username = username;
        this.password = password;
        this.userGroup = userGroup;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
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
}
