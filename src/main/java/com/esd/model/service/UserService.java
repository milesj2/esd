package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIdException;

import java.sql.SQLException;
import java.util.List;

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

    public List<User> getUsers() throws SQLException {
        return userDao.getUsers();
    }

    public User validateCredentials(String username, String password) throws SQLException, InvalidUserCredentialsException {
        User user = userDao.getUserByUsername(username);
        if(user.getPassword().equals(password)){
            return user;
        }
        throw new InvalidUserCredentialsException("Passwords don't match");
    }
    
    public boolean createUser(User user, UserDetails userDetails) throws SQLException, InvalidUserCredentialsException {
        boolean usernameUnique = userDao.verifyUsernameIsUnique(user.getUsername());
        if (usernameUnique) {
            userDao.createSystemUser(user);
            int userId = userDao.getUserIdFromUserName(user.getUsername());
            userDao.addUserDetailsToSystemUser(userId, userDetails);
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
