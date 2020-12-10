package com.esd.model.dao;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIDException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class UserDao {

    private static UserDao instance;
    private static final String GET_USERS = "select * from systemUser";
    private static final String GET_USER_BY_USERNAME = "select * from systemUser where systemUser.username=?";
    private static final String GET_USER_BY_ID = "select * from systemUser where systemUser.id=?";
    private static final String UPDATE_USER = "UPDATE SYSTEMUSER SET username=?,password=?,usergroup=?,active=? WHERE ID=?";

    private UserDao() {
    }

    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<User>();

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_USERS);
        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if(!resultFound){
            // throw database empty error or not?
        }

        while (resultFound)
        {

            User user = new User(
                    result.getInt(DaoConsts.SYSTEMUSER_ID),
                    result.getString(DaoConsts.SYSTEMUSER_USERNAME),
                    result.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                    UserGroup.valueOf(result.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                    result.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
            );
            users.add(user);
            resultFound = result.next();
        }

        return users;

    }

    /**
     * Gets a user by there username throws a null pointer exception if no user was found
     */
    public User getUserByUsername(String username) throws SQLException, InvalidUserCredentialsException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_USER_BY_USERNAME);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidUserCredentialsException("No user found for username");
        }
        return new User(
                result.getInt(DaoConsts.SYSTEMUSER_ID),
                result.getString(DaoConsts.SYSTEMUSER_USERNAME),
                result.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(result.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                result.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
        );
    }

    /**
     * Gets a user by their id. Throws a null pointer exception if no user was found
     */
    public User getUserByID(int id) throws SQLException, InvalidUserIDException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_USER_BY_ID);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidUserIDException(String.format("No user found for id '%d'", id));
        }
        User user = new User(
                result.getInt(DaoConsts.SYSTEMUSER_ID),
                result.getString(DaoConsts.SYSTEMUSER_USERNAME),
                result.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(result.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                result.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
        );
        return user;
    }

    public boolean updateUser(User user) throws SQLException, InvalidUserIDException{
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_USER);

        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getUserGroup().name());
        statement.setBoolean(4, user.isActive());
        statement.setInt(5, user.getId());


        int userResult = statement.executeUpdate();

        if (userResult == 1){
            return true;
        } else if (userResult == 0){
            throw new InvalidUserIDException(String.format("No user found for id '%d'", user.getId()));
        } else{
            // Uknown Exception?
            return false;
        }
    }

    public synchronized static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }
}
