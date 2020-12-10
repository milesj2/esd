package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserDetailsIDException;
import com.esd.model.exceptions.InvalidUserIDException;

import java.sql.SQLException;

/**
 * Original Author: Miles Jarvis
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user details
 */
public class UserDetailsService {
    private static UserDetailsService instance;
    private UserDetailsDao userDetailsDao;

    private UserDetailsService(UserDetailsDao userDetailsDao) {
        if(userDetailsDao == null){
            throw new IllegalArgumentException("userDetailsDao must not be null");
        }
        this.userDetailsDao = userDetailsDao;
    }

    public synchronized static UserDetailsService getInstance(){
        if(instance == null){
            instance = new UserDetailsService(UserDetailsDao.getInstance());
        }
        return instance;
    }


    public UserDetails getUserDetailsByUserID(int userId) throws SQLException, InvalidUserIDException {
        return UserDetailsDao.getInstance().getUserDetailsByUserId(userId);
    }

    public boolean updateUserDetails(UserDetails userDetails) throws InvalidUserDetailsIDException, SQLException {
        return UserDetailsDao.getInstance().updateUserDetails(userDetails);
    }

    public static UserDetailsService getTestInstance(UserDetailsDao userDetailsDao){
        return new UserDetailsService(userDetailsDao);
    }

}