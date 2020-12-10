package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIDException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user such as Authentication
 */
public class UserService {
    private static UserService instance;
    private UserDao userDao;

    private UserService(UserDao userDao) {
        if(userDao == null){
            throw new IllegalArgumentException("userdao must not be null");
        }
        this.userDao = userDao;
    }

    public ArrayList<User> getUsers() throws SQLException {
        return userDao.getUsers();
    }

    public User validateCredentials(String username, String password) throws SQLException, InvalidUserCredentialsException {
        User user = userDao.getUserByUsername(username);
        if(user.getPassword().equals(password)){
            return user;
        }
        throw new InvalidUserCredentialsException("Passwords don't match");
    }
    
    public boolean createUser(String username, String password, String active, String userGroup, 
        String firstName, String lastName, String addressLine1, String addressLine2, String addressLine3, String town, String postCode, String dob) 
        throws SQLException {
        
        UserDao db = UserDao.getInstance();
        boolean matchFound = db.verifyUsernameIsUnique(username);
        
        if (!matchFound) {
          db.addUser2SystemUser(username, password, active, userGroup);
          String userId =  db.getUserId(username);
          db.addUser2UserDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob);
          return true;
        }
        
        return false;
    }

    public User getUserByID(int id) throws SQLException, InvalidUserIDException {
        return userDao.getUserByID(id);
    }

    public boolean updateUser(User user) throws SQLException, InvalidUserIDException {
        return userDao.updateUser(user);
    }

    public synchronized static UserService getInstance(){
        if(instance == null){
            instance = new UserService(UserDao.getInstance());
        }
        return instance;
    }

    public static UserService getTestInstance(UserDao userdao){
        return new UserService(userdao);
    }
}
