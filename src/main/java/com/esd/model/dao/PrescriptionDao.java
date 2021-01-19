package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restriction;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.exceptions.InvalidIdValueException;
import org.joda.time.LocalDate;

import java.sql.*;
import java.sql.Date;
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
            "where id=? or originatingPrescriptionId=? and issueDate >= CURRENT_DATE";

    private PrescriptionDao() {
    }
    
    public synchronized static PrescriptionDao getInstance(){
        if(instance == null){
            instance = new PrescriptionDao();
        }
        return instance;
    }

    public Prescription getPrescriptionForAppointment(int appointmentId) throws SQLException {
        PreparedStatement statement = new SelectQueryBuilder(DaoConsts.TABLE_PRESCRIPTIONS)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.APPOINTMENT_ID_FK, appointmentId))
                .createStatement();
        ResultSet results = statement.executeQuery();
        if(results.next()){
            return getPrescriptionDetailsFromResults(results);
        }
        return null;
    }


    private Prescription getPrescriptionDetailsFromResults(ResultSet result) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setOriginatingPrescriptionId(result.getInt(DaoConsts.PRESCRIPTION_ORIGINATING_PRESCRIPTION_ID));
        prescription.setId(result.getInt(DaoConsts.ID));
        prescription.setEmployeeId(result.getInt(DaoConsts.EMPLOYEE_ID_FK));
        prescription.setPatientId(result.getInt(DaoConsts.PATIENT_ID_FK));
        prescription.setPrescriptionDetails(result.getString(DaoConsts.PRESCRIPTION_DETAILS));
        prescription.setIssueDate(LocalDate.parse(result.getString(DaoConsts.PRESCRIPTION_ISSUE_DATE)));
        return  prescription;
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
        if(prescription.getId()!=0){
            throw new InvalidIdValueException("prescription id must be 0 to create prescription");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT_INTO_PRESCRIPTIONS, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, prescription.getEmployeeId());
        statement.setInt(2, prescription.getPatientId());
        statement.setString(3, prescription.getPrescriptionDetails());
        statement.setInt(4, prescription.getAppointmentId());
        statement.setDate(5, Date.valueOf(prescription.getIssueDate().toString()));
        statement.executeUpdate();

        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        prescription.setId(keys.getInt(1));
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
        statement.setDate(5, Date.valueOf(prescription.getIssueDate().toString()));
        statement.setInt(6, prescription.getId());
        statement.setInt(7, prescription.getId());
        statement.executeUpdate();
    }


}
