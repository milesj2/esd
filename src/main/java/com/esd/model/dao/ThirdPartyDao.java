package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.joins.Joins;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.ReferalSearchDto;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.ThirdParty;
import com.esd.model.exceptions.InvalidIdValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to third party management
 */
public class ThirdPartyDao {
    private static ThirdPartyDao instance;
    
    private static final String INSERT_INTO_THIRDPARTY = "insert into thirdParty " +
            "(thirdPartyName, addressLine1, addressLine2, addressLine3, town, postCode, thirdPartyType, active) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_THIRDPARTY = "UPDATE THIRDPARTY SET " +
            "thirdPartyName=?" +
            ",addressline1=?" +
            ",addressline2=?" +
            ",addressline3=?" +
            ",town=?" +
            ",postCode=?" +
            ",thirdPartyType=?" +
            ",active=? " +
            "WHERE ID=?";
    
    private ThirdPartyDao() {
    }
    
    public List<ThirdParty> getThirdParties() throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_THIRDPARTY);

        return extractThirdPartiesFromResultSet(queryBuilder.createStatement());
    }
    
    public synchronized static ThirdPartyDao getInstance(){
        if(instance == null){
            instance = new ThirdPartyDao();
        }
        return instance;
    }
    
    
    public void createThirdParty(ThirdParty thirdParty){
        Connection con = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement statement = con.prepareStatement(INSERT_INTO_THIRDPARTY);
            statement.setString(1, thirdParty.getName());
            statement.setString(2, thirdParty.getAddressLine1());
            statement.setString(3, thirdParty.getAddressLine2());
            statement.setString(4, thirdParty.getAddressLine3());
            statement.setString(5, thirdParty.getTown());
            statement.setString(6, thirdParty.getPostCode());
            statement.setString(7, thirdParty.getType());
            statement.setBoolean(8, thirdParty.isActive());

            statement.executeUpdate();

        } catch(SQLException e) {
            System.err.println("Error: " + e);
        }
    }
    
    private List<ThirdParty> extractThirdPartiesFromResultSet(PreparedStatement statement) throws SQLException {
        List<ThirdParty> returnValue = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while(result.next()){
            returnValue.add(getThirdPartyFromResults(result));
        }
        return returnValue;
    }
    
    private ThirdParty getThirdPartyFromResults(ResultSet result) throws SQLException {
        return new ThirdParty(
                result.getInt(DaoConsts.ID),
                result.getString(DaoConsts.THIRDPARTY_NAME),
                result.getString(DaoConsts.THIRDPARTY_ADDRESS1),
                result.getString(DaoConsts.THIRDPARTY_ADDRESS2),
                result.getString(DaoConsts.THIRDPARTY_ADDRESS3),
                result.getString(DaoConsts.THIRDPARTY_TOWN),
                result.getString(DaoConsts.THIRDPARTY_POSTCODE),
                result.getString(DaoConsts.THIRDPARTY_TYPE),
                result.getBoolean(DaoConsts.THIRDPARTY_ACTIVE)
        );
    }
    
    public ThirdParty getThirdPartyByID(int id) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_THIRDPARTY)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));

        List<ThirdParty> ThirdParties = extractThirdPartyFromResultSet(queryBuilder.createStatement());
        if(ThirdParties.size() != 1){
            throw new InvalidIdValueException(String.format("No third aprty found for id '%d'", id));
        }
        return ThirdParties.get(0);

    }
    
    private List<ThirdParty> extractThirdPartyFromResultSet(PreparedStatement statement) throws SQLException {
        List<ThirdParty> returnValue = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while(result.next()){
            returnValue.add(getThirdPartyFromResults(result));
        }
        return returnValue;
    }
    
    public boolean updateThirdParty(ThirdParty thirdParty) throws SQLException, InvalidIdValueException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_THIRDPARTY);

        statement.setString(1, thirdParty.getName());
        statement.setString(2, thirdParty.getAddressLine1());
        statement.setString(3, thirdParty.getAddressLine2());
        statement.setString(4, thirdParty.getAddressLine3());
        statement.setString(5, thirdParty.getTown());
        statement.setString(6, thirdParty.getPostCode());
        statement.setString(7, thirdParty.getType());
        statement.setBoolean(8, thirdParty.isActive());
        statement.setInt(9, thirdParty.getId());


        int result = statement.executeUpdate();

        if (result == 1){
            return true;
        } else if (result == 0){
            throw new InvalidIdValueException(String.format("No third party details found for id '%d'", thirdParty.getId()));
        } else {
            //throw custom error
            return false;
        }
    }

    public ArrayList<ReferalSearchDto> getReferalsByFilteredResults(SystemUser currentUser, Map<String, Object> args) throws SQLException {
    //this stack overflow page explains why this looks a little odd
    //https://stackoverflow.com/questions/7224024/jdbc-resultset-get-columns-with-table-alias
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS, "A")
                .selectColumn("A." + DaoConsts.ID)
                .selectColumn("A." + DaoConsts.APPOINTMENT_DATE)
                .selectColumn("A." + DaoConsts.APPOINTMENT_TIME)
                .selectColumn("E." + DaoConsts.USERDETAILS_FIRSTNAME)
                .selectColumn("E." + DaoConsts.USERDETAILS_LASTNAME)
                .selectColumn("P." + DaoConsts.USERDETAILS_FIRSTNAME)
                .selectColumn("P." + DaoConsts.USERDETAILS_LASTNAME)
                .selectColumn("T." + DaoConsts.THIRDPARTY_NAME)
                .selectColumn("T." + DaoConsts.THIRDPARTY_ADDRESS1)
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_THIRDPARTY, "T", "A." + DaoConsts.THIRDPARTY_ID_FK, "T."+DaoConsts.ID))
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_USERDETAILS, "P", "A." + DaoConsts.PATIENT_ID_FK, "P."+DaoConsts.ID))
                .withJoin(Joins.innerJoin(DaoConsts.TABLE_USERDETAILS, "E", "A." + DaoConsts.EMPLOYEE_ID_FK, "E."+DaoConsts.ID));


        Iterator mapIter = args.entrySet().iterator();
        while(mapIter.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIter.next();
            queryBuilder.and(Restrictions.like(pair.getKey().toString(), pair.getValue()));
        }

        switch(currentUser.getUserGroup()){
            case NHS_PATIENT:
            case PRIVATE_PATIENT:
                queryBuilder.withRestriction(Restrictions.equalsRestriction("P." + DaoConsts.ID, currentUser.getUserDetails().getId()));
                break;
        }

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();

        ArrayList<ReferalSearchDto> referalSearchDtos = new ArrayList<>();
        // add results to list of user to return
        while (result.next()) {
            referalSearchDtos.add(getReferalSearchDtoFromResult(result));
        }

        return referalSearchDtos;
    }

    private ReferalSearchDto getReferalSearchDtoFromResult(ResultSet result) throws SQLException {
        ReferalSearchDto dto = new ReferalSearchDto();
        dto.setId(result.getInt("A_" + DaoConsts.ID));
        dto.setAppointmentDate(LocalDate.parse(result.getString("A_" + DaoConsts.APPOINTMENT_DATE)));
        dto.setAppointmentTime(LocalTime.parse(result.getString("A_" + DaoConsts.APPOINTMENT_TIME)));
        dto.setPractitionerFirstName(result.getString("E_" + DaoConsts.USERDETAILS_FIRSTNAME));
        dto.setPractitionerLastName(result.getString("E_" + DaoConsts.USERDETAILS_LASTNAME));
        dto.setPatientFirstName(result.getString("P_" + DaoConsts.USERDETAILS_FIRSTNAME));
        dto.setPatientLastName(result.getString("P_" + DaoConsts.USERDETAILS_LASTNAME));
        dto.setThirdPartyName(result.getString("T_" + DaoConsts.THIRDPARTY_NAME));
        dto.setThirdPartyAdress1(result.getString("T_" + DaoConsts.THIRDPARTY_ADDRESS1));

        return dto;
    }

}
