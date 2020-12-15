package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIdException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user such as Authentication
 */
public class UserService {
    private static UserService instance;

    
    private UserDao userDao = UserDao.getInstance();

    private UserService() {   

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
        String firstName, String lastName, String addressLine1, String addressLine2, String addressLine3, String town, String postCode, Date dob) 
        throws SQLException, InvalidUserIdException {
        boolean matchFound = userDao.verifyUsernameIsUnique(username);
        if (!matchFound) {
            userDao.addUser2SystemUser(username, password, active, userGroup);
            int userId = userDao.getUserId(username);
            userDao.addUserDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob);
            return true;
        }
        return false;
    }

    public User getUserByID(int id) throws SQLException, InvalidIdValueException {
        return userDao.getUserByID(id);
    }

    public boolean updateUser(User user) throws SQLException, InvalidIdValueException {
        return userDao.updateUser(user);
    }

    public synchronized static UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }
}
