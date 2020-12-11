package com.esd.model.dao;

import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Original Author: Miles Jarvis
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to UserDetails
 */
public class UserDetailsDao {
    private static UserDetailsDao instance;

    private static final String GET_USER_BY_USER_ID = "select * from userDetails where userDetails.userid=?";
    private static final String UPDATE_USER_DETAILS = "UPDATE USERDETAILS SET " +
            "firstname=?" +
            ",lastname=?" +
            ",addressline1=?" +
            ",addressline2=?" +
            ",addressline3=?" +
            ",town=?" +
            ",postcode=?" +
            ",dob=? " +
            "WHERE USERID=?";

    private UserDetailsDao() {
    }

    public synchronized static UserDetailsDao getInstance(){
        if(instance == null){
            instance = new UserDetailsDao();
        }
        return instance;
    }

    private UserDetails getUserDetailsFromResults(ResultSet result) throws SQLException {
        return new UserDetails(
                result.getInt(DaoConsts.USERDETAILS_ID),
                result.getInt(DaoConsts.SYSTEMUSER_ID),
                result.getString(DaoConsts.USERDETAILS_FIRSTNAME),
                result.getString(DaoConsts.USERDETAILS_LASTNAME),
                result.getString(DaoConsts.USERDETAILS_ADDRESS1),
                result.getString(DaoConsts.USERDETAILS_ADDRESS2),
                result.getString(DaoConsts.USERDETAILS_ADDRESS3),
                result.getString(DaoConsts.USERDETAILS_TOWN),
                result.getString(DaoConsts.USERDETAILS_POSTCODE),
                result.getString(DaoConsts.USERDETAILS_DOB)
        );
    }

    public UserDetails getUserDetailsByUserId(int id) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_USER_BY_USER_ID);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidIdValueException(String.format("No user details found for id '%d'", id));
        }
        return getUserDetailsFromResults(result);
    }

    public boolean updateUserDetails(UserDetails userDetails) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(UPDATE_USER_DETAILS);

        statement.setString(1, userDetails.getFirstName());
        statement.setString(2, userDetails.getLastName());
        statement.setString(3, userDetails.getAddressLine1());
        statement.setString(4, userDetails.getAddressLine2());
        statement.setString(5, userDetails.getAddressLine3());
        statement.setString(6, userDetails.getTown());
        statement.setString(7, userDetails.getPostCode());
        statement.setString(8, userDetails.getDOB());
        statement.setInt(9, userDetails.getUserId());

        int result = statement.executeUpdate();

        if (result == 1){
            return true;
        } else if (result == 0){
            throw new InvalidIdValueException(String.format("No user details found for id '%d'", userDetails.getUserId()));
        } else {
            //throw custom error
            return false;
        }
    }
}
