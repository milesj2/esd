package com.esd.model.service;

import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

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


    public UserDetails getUserDetailsByUserID(int userId) throws SQLException, InvalidIdValueException {
        return UserDetailsDao.getInstance().getUserDetailsByUserId(userId);
    }

    public boolean updateUserDetails(UserDetails userDetails) throws InvalidIdValueException, SQLException {
        return UserDetailsDao.getInstance().updateUserDetails(userDetails);
    }

    public static UserDetailsService getTestInstance(UserDetailsDao userDetailsDao){
        return new UserDetailsService(userDetailsDao);
    }

    public static ArrayList<UserDetails> getUserDetailsFromFilteredRequest(Map<String, Object> args) throws SQLException {
        ArrayList<UserDetails> userDetails = UserDetailsDao.getInstance().getFilteredDetails(args);
        return userDetails;
    }
}