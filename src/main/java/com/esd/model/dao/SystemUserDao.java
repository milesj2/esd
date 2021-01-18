package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.joins.Joins;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class SystemUserDao {

    private static SystemUserDao instance;
    private static final String INSERT_INTO_SYSTEMUSER = "insert into systemUser " +
            "(username, password, active, userGroup) values (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE SYSTEMUSER SET " +
            "username=?,password=?,usergroup=?,active=? WHERE ID=?";

    private SystemUserDao() {
    }

    public List<SystemUser> getUsers() throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER);

        return extractUsersFromResultSet(queryBuilder.createStatement());
    }

    public SystemUser getUserByUsername(String username) throws SQLException, InvalidUserCredentialsException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.SYSTEMUSER_USERNAME, username));

        List<SystemUser> systemUsers = extractUsersFromResultSet(queryBuilder.createStatement());
        if(systemUsers.size() != 1){
            throw new InvalidUserCredentialsException("No user found for username");
        }
        return systemUsers.get(0);
    }

    public SystemUser getUserByID(int id) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));

        List<SystemUser> systemUsers = extractUsersFromResultSet(queryBuilder.createStatement());
        if(systemUsers.size() != 1){
            throw new InvalidIdValueException(String.format("No user found for id '%d'", id));
        }
        return systemUsers.get(0);

    }

    public SystemUser getUserByUserDetailsID(int userDetailsid) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_USERDETAILS, DaoConsts.SYSTEMUSER_ID_FK, DaoConsts.TABLE_SYSTEMUSER_REFERENCE + DaoConsts.ID))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.TABLE_USERDETAILS_REFERENCE + DaoConsts.ID, userDetailsid));

        List<SystemUser> systemUsers = extractUsersFromResultSet(queryBuilder.createStatement());
        if(systemUsers.size() != 1){
            throw new InvalidIdValueException(String.format("No user found for id '%d'", userDetailsid));
        }
        return systemUsers.get(0);

    }

    public boolean updateUser(SystemUser systemUser) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_USER);

        statement.setString(1, systemUser.getUsername());
        statement.setString(2, systemUser.getPassword());
        statement.setString(3, systemUser.getUserGroup().name());
        statement.setBoolean(4, systemUser.isActive());
        statement.setInt(5, systemUser.getId());

        int userResult = statement.executeUpdate();

        if (userResult == 1){
            return true;
        } else if (userResult == 0){
            throw new InvalidIdValueException(String.format("No user found for id '%d'", systemUser.getId()));
        } else{
            // Uknown Exception?
            return false;
        }
    }
    
    public boolean verifyUsernameIsUnique(String username) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.SYSTEMUSER_USERNAME, username));

        List<SystemUser> systemUsers = extractUsersFromResultSet(queryBuilder.createStatement());

        return systemUsers.size() == 0;
    }
    
    public void createSystemUser(SystemUser systemUser){

        Connection con = ConnectionManager.getInstance().getConnection();
        
        try {
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_SYSTEMUSER);
        statement.setString(1, systemUser.getUsername());
        statement.setString(2, systemUser.getPassword());
        statement.setBoolean(3, systemUser.isActive());
        statement.setString(4, systemUser.getUserGroup().name());
        int resultAdded = statement.executeUpdate();
        
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }
    }
    
     public int getUserIdFromUserName(String userName) throws SQLException, InvalidUserCredentialsException {
        return getUserByUsername(userName).getId();
    }
    


    private List<SystemUser> extractUsersFromResultSet(PreparedStatement statement) throws SQLException {
        List<SystemUser> returnValue = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while(result.next()){
            returnValue.add(getUserFromResults(result));
        }
        return returnValue;
    }

    public SystemUser getUserFromResults(ResultSet resultSet) throws SQLException {
        SystemUser systemUser = new SystemUser();
        return new SystemUser(
                resultSet.getInt(DaoConsts.ID),
                resultSet.getString(DaoConsts.SYSTEMUSER_USERNAME),
                resultSet.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(resultSet.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                resultSet.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
        );
    }
    
    public synchronized static SystemUserDao getInstance(){
        if(instance == null){
            instance = new SystemUserDao();
        }
        return instance;
    }
}
