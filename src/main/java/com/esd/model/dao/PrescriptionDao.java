package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to prescriptions
 */

public class PrescriptionDao {
    
    private static PrescriptionDao instance;
    private static final String INSERT_INTO_PRESCRIPTIONS = "insert into prescriptions " +
            "(employeeId, patientId, prescriptionDetails, appointmentId, issueDate) " +
            "values (?, ?, ?, ?, ?)";
    private static final String UPDATE_PRESCRIPTIONS = "update prescriptions set "+
            "employeeId=?, "+
            "patientid=?, "+
            "prescriptiondetails=?, "+
            "appointmentid=?, "+
            "issuedate=? "+
            "where id=?";

    private PrescriptionDao() {
    }
    
    public synchronized static PrescriptionDao getInstance(){
        if(instance == null){
            instance = new PrescriptionDao();
        }
        return instance;
    }

    private Prescription getPrescriptionDetailsFromResults(ResultSet result) throws SQLException {
        return new Prescription(
            result.getInt(DaoConsts.PRESCRIPTION_ID),
            result.getInt(DaoConsts.EMPLOYEE_ID_FK),
            result.getInt(DaoConsts.PATIENT_ID_FK),
            result.getString(DaoConsts.PRESCRIPTION_DETAILS), 
            result.getInt(DaoConsts.ID),
            result.getDate(DaoConsts.PRESCRIPTION_ISSUE_DATE)
        );
    }

    public List<Prescription> getFilteredDetails(Map<String, Object> args) throws SQLException {
        ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_PRESCRIPTIONS);

        Iterator mapIter = args.entrySet().iterator();
        while(mapIter.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIter.next();
            queryBuilder.and(Restrictions.equalsRestriction(pair.getKey().toString(), pair.getValue()));
        }

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();

        // add results to list of prescriptions to return
        while(result.next()){
            prescriptionList.add(getPrescriptionDetailsFromResults(result));
        }

        return prescriptionList;
    }

    //For creating new prescription     
    public void createPrescription(Prescription prescription) throws SQLException, InvalidIdValueException {
        if(prescription.getId()==0){
            throw new InvalidIdValueException("prescription id must be 0 to create prescription");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_PRESCRIPTIONS);
        statement.setInt(1, prescription.getEmployeeId());
        statement.setInt(2, prescription.getPatientId());
        statement.setString(3, prescription.getPrescriptionDetails());
        statement.setInt(4, prescription.getAppointmentId());
        statement.setDate(5, (java.sql.Date) prescription.getIssueDate());
        statement.executeUpdate();
    }

    public void updatePrescription(Prescription prescription) throws SQLException, InvalidIdValueException {
        if(prescription.getId()!=0){
            throw new InvalidIdValueException("prescription id must not be 0 to update prescription");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_PRESCRIPTIONS);
        statement.setInt(1, prescription.getEmployeeId());
        statement.setInt(2, prescription.getPatientId());
        statement.setString(3, prescription.getPrescriptionDetails());
        statement.setInt(4, prescription.getAppointmentId());
        statement.setDate(5, (java.sql.Date) prescription.getIssueDate());
        statement.setInt(6, prescription.getId());
        statement.executeUpdate();
    }
}
