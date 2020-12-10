package com.esd.model.dao;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class UserDao {

    private static UserDao instance;
    private static final String GET_USER_BY_USERNAME = "select * from systemUser where systemUser.username=?";
    private static final String GET_ID_BY_USERNAME = "select ID from systemUser where systemUser.username=?";
    private static final String INSERT_INTO_SYSTEMUSER = "insert into systemUser (username, password, active, userGroup) values (?, ?, ?, ?)";
    private static final String INSERT_INTO_USERDETAILS = "insert into userDetails (userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private Statement state;

    private UserDao() {
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
                result.getString(DaoConsts.SYSTEMUSER_ID),
                result.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(result.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                result.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
        );
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
