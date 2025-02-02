package com.esd.model.service;

import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public UserDetails getUserDetailsByID(int id)  {
        try {
            return userDetailsDao.getUserDetailsById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserDetails getUserDetailsByUserID(int userId) {
        try {
            return userDetailsDao.getUserDetailsByUserId(userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserDetails> getAllUsersOfGroups(UserGroup... groups) throws SQLException, InvalidIdValueException {
        return userDetailsDao.getAllUsersOfGroups(groups);
    }

    public boolean updateUserDetails(UserDetails userDetails) throws InvalidIdValueException, SQLException {
        return userDetailsDao.updateUserDetails(userDetails);
    }

    public static UserDetailsService getTestInstance(UserDetailsDao userDetailsDao){
        return new UserDetailsService(userDetailsDao);
    }

    public List<UserDetails> getUserDetailsFromFilteredRequest(SystemUser currentUser, Map<String, Object> args) {
        List<UserDetails> userDetails = new ArrayList<>();
        try {
            userDetails = UserDetailsDao.getInstance().getFilteredDetails(currentUser, args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userDetails;
    }
}