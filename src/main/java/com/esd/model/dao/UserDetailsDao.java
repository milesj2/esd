package com.esd.model.dao;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIDException;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Miles Jarvis
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to UserDetails
 */
public class UserDetailsDao {
    private static UserDetailsDao instance;

    private static final String GET_USER_BY_USER_ID = "select * from userDetails where userDetails.userid=?";
    private static final String GET_FILTERED_USERS = "SELECT * FROM USERDETAILS";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String MATCH = " = ?";

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

    public UserDetails getUserDetailsByUserId(int id) throws SQLException, InvalidUserIDException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(GET_USER_BY_USER_ID);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();

        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidUserIDException("No user found for user id");
        }
        return getUserDetailsFromResults(result);
    }

    public static ArrayList<UserDetails> getFilteredDetails(ArrayList<String> formKey, HttpServletRequest request){

        ArrayList<UserDetails> userDetailsList = new ArrayList<UserDetails>();
        String STATEMENT_BUILDER = GET_FILTERED_USERS;

        try {
            boolean first = true;
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    if(first){
                        STATEMENT_BUILDER += WHERE+key+MATCH;
                        first = false;
                    } else {
                        STATEMENT_BUILDER += AND+key+MATCH;
                    }
                }
            }

            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(STATEMENT_BUILDER);

            int i=1;  //set statement values
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    statement.setString(i,(String)request.getParameter(key));
                    i+=1;
                }
            }

            ResultSet result = statement.executeQuery();

            // add results to list of user to return
            while(result.next()){
                UserDetails userDetails =  new UserDetails(
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
                userDetailsList.add(userDetails);
            }

            // close statement and result set
            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userDetailsList;
    }
}
