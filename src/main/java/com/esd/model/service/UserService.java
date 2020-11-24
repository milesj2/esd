package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.data.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.SQLException;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user such as Authentication
 */
public class UserService {
    private static UserService instance;
    private UserDao userdao;

    private UserService(UserDao userdao) {
        if(userdao == null){
            throw new IllegalArgumentException("userdao must not be null");
        }
        this.userdao = userdao;
    }

    public User validateCredentials(String username, String password) throws SQLException, InvalidUserCredentialsException {
        User user = userdao.getUserByUsername(username);
        if(user.getPassword().equals(password)){
            return user;
        }
        throw new InvalidUserCredentialsException("Passwords don't match");
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
