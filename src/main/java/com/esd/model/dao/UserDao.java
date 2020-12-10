package com.esd.model.dao;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.DuplicateUserException;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIDException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
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
    
    public boolean verifyUsernameIsUnique(String username) {
        boolean matchFound = false;
        try {        
          Connection con = ConnectionManager.getInstance().getConnection();
          PreparedStatement statement = con.prepareStatement(GET_USER_BY_USERNAME);
          statement.setString(1, username);
          ResultSet result = statement.executeQuery();
          matchFound = result.next();
        }catch (SQLException e) {
           System.err.println("Error: " + e);
        }
        return matchFound;
    }
    
    public void addUser2SystemUser(String username, String password, String active, String userGroup){

        Connection con = ConnectionManager.getInstance().getConnection();
        
        try {
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_SYSTEMUSER);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, active);
        statement.setString(4, userGroup);
        int resultAdded = statement.executeUpdate();
        
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }
    }
    
     public String getUserId(String dataUserName) {
        String userid = "";
        
        try {        
          Connection con = ConnectionManager.getInstance().getConnection();
          PreparedStatement statement = con.prepareStatement(GET_ID_BY_USERNAME);
          statement.setString(1, dataUserName);
          ResultSet rs = statement.executeQuery();

          while (rs.next()) {
            userid = rs.getString(1);
           }

        }catch (SQLException e) {
           System.err.println("Error: " + e);
        }
        return userid;
    }
    
    public void addUser2UserDetails(String userId, String firstName, String lastName, String addressLine1, String addressLine2, String addressLine3, String town, String postCode, String dob){

        Connection con = ConnectionManager.getInstance().getConnection();
        
        try {
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_USERDETAILS);
        statement.setString(1, userId);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, addressLine1);
        statement.setString(5, addressLine2);
        statement.setString(6, addressLine3);
        statement.setString(7, town);
        statement.setString(8, postCode);
        statement.setString(9, dob);
        int checkUserAddedToUserDetails = statement.executeUpdate();
        
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }        
    }

    public synchronized static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }
}
