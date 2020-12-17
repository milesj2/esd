package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.exceptions.InvalidUserIdException;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Original Author: Jordan Hellier
 * Modified by: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to users
 */
public class UserDao {

    private static UserDao instance;
    private static final String INSERT_INTO_SYSTEMUSER = "insert into systemUser " +
            "(username, password, active, userGroup) values (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE SYSTEMUSER SET " +
            "username=?,password=?,usergroup=?,active=? WHERE ID=?";

    private UserDao() {
    }

    public List<User> getUsers() throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER);

        return extractUsersFromResultSet(queryBuilder.createStatement());
    }

    public User getUserByUsername(String username) throws SQLException, InvalidUserCredentialsException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.SYSTEMUSER_USERNAME, username));

        List<User> users = extractUsersFromResultSet(queryBuilder.createStatement());
        if(users.size() != 1){
            throw new InvalidUserCredentialsException("No user found for username");
        }
        return users.get(0);
    }

    public User getUserByID(int id) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));

        List<User> users = extractUsersFromResultSet(queryBuilder.createStatement());
        if(users.size() != 1){
            throw new InvalidIdValueException(String.format("No user found for id '%d'", id));
        }
        return users.get(0);

    }

    public boolean updateUser(User user) throws SQLException, InvalidIdValueException {
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
            throw new InvalidIdValueException(String.format("No user found for id '%d'", user.getId()));
        } else{
            // Uknown Exception?
            return false;
        }
    }
    
    public boolean verifyUsernameIsUnique(String username) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_SYSTEMUSER)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.SYSTEMUSER_USERNAME, username));

        List<User> users = extractUsersFromResultSet(queryBuilder.createStatement());

        return users.size() == 0;
    }
    
    public void createSystemUser(User user){

        Connection con = ConnectionManager.getInstance().getConnection();
        
        try {
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_SYSTEMUSER);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setBoolean(3, user.isActive());
        statement.setString(4, user.getUserGroup().name());
        int resultAdded = statement.executeUpdate();
        
        } catch(SQLException e) {
          System.err.println("Error: " + e);
        }
    }
    
     public int getUserIdFromUserName(String userName) throws SQLException, InvalidUserCredentialsException {
        return getUserByUsername(userName).getId();
    }
    


    private List<User> extractUsersFromResultSet(PreparedStatement statement) throws SQLException {
        List<User> returnValue = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while(result.next()){
            returnValue.add(getUserFromResults(result));
        }
        return returnValue;
    }

    public User getUserFromResults(ResultSet resultSet) throws SQLException {
        User user = new User();
        return new User(
                resultSet.getInt(DaoConsts.ID),
                resultSet.getString(DaoConsts.SYSTEMUSER_USERNAME),
                resultSet.getString(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(resultSet.getString(DaoConsts.SYSTEMUSER_USERGROUP)),
                resultSet.getBoolean(DaoConsts.SYSTEMUSER_ACTIVE)
        );
    }
    
    public synchronized static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }
}
