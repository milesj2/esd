package com.esd.model.service;

import com.esd.model.dao.UserDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIDException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user such as Authentication
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


    public static UserDetails getUserDetailsByUserID(int userId) throws SQLException, InvalidUserIDException {
        UserDetails userDetails = UserDetailsDao.getInstance().getUserDetailsByUserId(userId);
        return userDetails;
    }

    public static UserDetailsService getTestInstance(UserDetailsDao userDetailsDao){
        return new UserDetailsService(userDetailsDao);
    }

    public static ArrayList<UserDetails> getUserDetailsFromFilteredRequest(ArrayList<String> formKeys,
                                                                           HttpServletRequest request) {
        ArrayList<UserDetails> userDetails = UserDetailsDao.getInstance().getFilteredDetails(formKeys, request);
        return userDetails;
    }
}