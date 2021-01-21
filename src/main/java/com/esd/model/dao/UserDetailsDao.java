package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.joins.Joins;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import org.joda.time.LocalDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Original Author: Miles Jarvis
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to UserDetails
 */
public class UserDetailsDao {
    private static UserDetailsDao instance;

    private static final String INSERT_INTO_USERDETAILS = "insert into userDetails " +
            "(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                result.getInt(DaoConsts.ID),
                result.getInt(DaoConsts.ID),
                result.getString(DaoConsts.USERDETAILS_FIRSTNAME),
                result.getString(DaoConsts.USERDETAILS_LASTNAME),
                result.getString(DaoConsts.USERDETAILS_ADDRESS1),
                result.getString(DaoConsts.USERDETAILS_ADDRESS2),
                result.getString(DaoConsts.USERDETAILS_ADDRESS3),
                result.getString(DaoConsts.USERDETAILS_TOWN),
                result.getString(DaoConsts.USERDETAILS_POSTCODE),
                LocalDate.parse(result.getString(DaoConsts.USERDETAILS_DOB))
        );
    }

    public UserDetails getUserDetailsById(int id) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_USERDETAILS)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();
        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidIdValueException(String.format("No user details found for id '%d'", id));
        }
        return getUserDetailsFromResults(result);
    }

    public UserDetails getUserDetailsByUserId(int userId) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_USERDETAILS)
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_SYSTEMUSER, DaoConsts.SYSTEMUSER_ID_FK, DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID, userId));

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();
        boolean resultFound = result.next();
        if(!resultFound){
            throw new InvalidIdValueException(String.format("No user details found for id '%d'", userId));
        }
        return getUserDetailsFromResults(result);
    }

    public boolean validateUserDetailsExistByIdAndUserGroup(int id, UserGroup... userGroups) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_USERDETAILS)
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_SYSTEMUSER, DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID, DaoConsts.SYSTEMUSER_ID_FK))
                .withRestriction(Restrictions.in(DaoConsts.SYSTEMUSER_USERGROUP, userGroups))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.TABLE_USERDETAILS_REFERENCE + DaoConsts.ID, id));

        PreparedStatement statement =  queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();
        return result.next();
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
        statement.setDate(8, Date.valueOf(userDetails.getDateOfBirth().toString()));
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

    public List<UserDetails> getAllUsersOfGroups(UserGroup...groups) throws SQLException {
        ArrayList<UserDetails> userDetailsList = new ArrayList<>();
        PreparedStatement statement = new SelectQueryBuilder(DaoConsts.TABLE_USERDETAILS)
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_SYSTEMUSER, DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID, DaoConsts.SYSTEMUSER_ID_FK))
                .withRestriction(Restrictions.in(DaoConsts.SYSTEMUSER_USERGROUP, groups))
                .createStatement();

        ResultSet result = statement.executeQuery();

        // add results to list of user to return
        while (result.next()) {
            userDetailsList.add(getUserDetailsFromResults(result));
        }

        return userDetailsList;
    }

   public List<UserDetails> getFilteredDetails(SystemUser currentUser, Map<String, Object> args) throws SQLException {

       ArrayList<UserDetails> userDetailsList = new ArrayList<UserDetails>();
       SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_USERDETAILS)
               .withJoin(Joins.innerJoin(DaoConsts.TABLE_SYSTEMUSER, DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID, DaoConsts.SYSTEMUSER_ID_FK));

       Iterator mapIter = args.entrySet().iterator();
       while(mapIter.hasNext()) {
           Map.Entry pair = (Map.Entry)mapIter.next();
           queryBuilder.and(Restrictions.like(pair.getKey().toString(), pair.getValue()));
       }

       switch(currentUser.getUserGroup()){
           case NHS_PATIENT:
           case PRIVATE_PATIENT:
               queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, currentUser.getUserDetails().getId()));
               break;
       }

       PreparedStatement statement = queryBuilder.createStatement();
       ResultSet result = statement.executeQuery();

       // add results to list of user to return
       while (result.next()) {
           userDetailsList.add(getUserDetailsFromResults(result));
       }

       return userDetailsList;
   }

    public void addUserDetailsToSystemUser(int userId, UserDetails userDetails){
        Connection con = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement statement = con.prepareStatement(INSERT_INTO_USERDETAILS);
            statement.setInt(1, userId);
            statement.setString(2, userDetails.getFirstName());
            statement.setString(3, userDetails.getLastName());
            statement.setString(4, userDetails.getAddressLine1());
            statement.setString(5, userDetails.getAddressLine2());
            statement.setString(6, userDetails.getAddressLine3());
            statement.setString(7, userDetails.getTown());
            statement.setString(8, userDetails.getPostCode());
            statement.setDate(9, Date.valueOf(userDetails.getDateOfBirth().toString()));

            statement.executeUpdate();

        } catch(SQLException e) {
            System.err.println("Error: " + e);
        }
    }
}
