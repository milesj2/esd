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
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class UserDao {

    private static UserDao instance;
    private static final String GET_USER_BY_USERNAME = "select * from systemUser where systemUser.username=?";
    private static final String GET_ID_BY_USERNAME = "select ID from systemUser where systemUser.username=?";
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
    
    public int addUser2SystemUser(String data){
        int flag = 0;
        
        try {
        Connection con = ConnectionManager.getInstance().getConnection();
        state = con.createStatement();
        flag = state.executeUpdate("insert into systemUser (username, password, userGroup) values" + data);
        state.close();
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }//try
        return (flag);
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
    
    public int addUser2UserDetails(String data2){
        int flag2 = 0;
        
        try {
        Connection con = ConnectionManager.getInstance().getConnection();
        state = con.createStatement();
        flag2 = state.executeUpdate("insert into userDetails (userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob) values" + data2);
        state.close();
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }//try
        return (flag2);
    }

    public synchronized static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }
}
