package com.esd.model.service;

import com.esd.model.dao.SystemUserDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InactiveAccountException;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.SQLException;
import java.util.List;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to do any functionality needed for user such as Authentication
 */
public class SystemUserService {
    private static SystemUserService instance;

    private SystemUserDao systemUserDao = SystemUserDao.getInstance();
    private UserDetailsDao userDetailsDao;

    private SystemUserService() {

    }

    public List<SystemUser> getUsers() throws SQLException {
        return systemUserDao.getUsers();
    }

    public SystemUser validateCredentials(String username, String password) throws SQLException, InvalidUserCredentialsException, InactiveAccountException {
        SystemUser systemUser = systemUserDao.getUserByUsername(username);

        if(systemUser.getPassword().equals(password)){
            if(!systemUser.isActive()){
                throw new InactiveAccountException("Account inactive");
            }
            return systemUser;
        }

        throw new InvalidUserCredentialsException("Passwords don't match");
    }
    
    public boolean createUser(SystemUser systemUser, UserDetails userDetails) throws SQLException, InvalidUserCredentialsException {
        boolean usernameUnique = systemUserDao.verifyUsernameIsUnique(systemUser.getUsername());
        if (usernameUnique) {
            systemUserDao.createSystemUser(systemUser);
            int userId = systemUserDao.getUserIdFromUserName(systemUser.getUsername());
            userDetailsDao.getInstance().addUserDetailsToSystemUser(userId, userDetails);
            return true;
        }
        return false;
    }

    public SystemUser getUserByID(int id) throws SQLException, InvalidIdValueException {
        return systemUserDao.getUserByID(id);
    }

    public SystemUser getUserByUserDetailsId(int id) {
        try {
            return systemUserDao.getUserByUserDetailsID(id);
        } catch (SQLException | InvalidIdValueException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(SystemUser systemUser) throws SQLException, InvalidIdValueException {
        return systemUserDao.updateUser(systemUser);
    }

    public boolean updatePassword(String password, int userId) throws SQLException, InvalidIdValueException {
        return systemUserDao.updatePassword(password, userId);
    }

    public boolean deleteUser(int id) throws SQLException {
        return systemUserDao.deleteUser(id);
    }

    public synchronized static SystemUserService getInstance(){
        if(instance == null){
            instance = new SystemUserService();
        }
        return instance;
    }
}
