package com.esd.model.dao;

import com.esd.model.data.User;
import com.esd.model.data.UserGroup;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class UserDao {

    private static UserDao instance;
    private static final String GET_USER_BY_USERNAME = "select * from tblUser where tblUser.username=?";



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
        //TODO validate only one user found
        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidUserCredentialsException("no user found for username");
        }
        User user = new User(
                result.getString("username"),
                result.getString("password"),
                UserGroup.valueOf(result.getString("usergroup"))
        );
        return user;
    }

    public synchronized static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }
}
