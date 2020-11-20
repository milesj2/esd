package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.data.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.SQLException;

public class UserService {
    private static UserService instance;
    private UserDao userdao = UserDao.getInstance();

    private UserService() {
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
            instance = new UserService();
        }
        return instance;
    }
}
