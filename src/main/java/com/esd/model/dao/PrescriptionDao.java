package com.esd.model.dao;

import com.esd.model.data.persisted.Prescription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to prescriptions
 */

public class PrescriptionDao {
    
    private static PrescriptionDao instance;
    

    private static final String INSERT_INTO_PRESCRIPTIONS = "insert into prescriptions (employeeId, patientId, prescriptionDetails, appointmentId, issueDate) values (?, ?, ?, ?, ?)";
    private static final String GET_All_PRESCRIPTIONS = "SELECT * FROM PRESCRIPTIONS";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String MATCH = " = ?";
    
    
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
            result.getInt(DaoConsts.APPOINTMENT_ID_FK),
            result.getDate(DaoConsts.PRESCRIPTION_ISSUE_DATE)
        );
    }
    
    //For Searching prescription
    public static ArrayList<Prescription> getFilteredDetails(ArrayList<String> formKey, HttpServletRequest request){

        ArrayList<Prescription> prescriptionList = new ArrayList<Prescription>();
        String STATEMENT_BUILDER = GET_All_PRESCRIPTIONS;

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

            // add results to list of prescriptions to return
            while(result.next()){
                Prescription prescription =  new Prescription (
                    result.getInt(DaoConsts.PRESCRIPTION_ID),
                    result.getInt(DaoConsts.EMPLOYEE_ID_FK),
                    result.getInt(DaoConsts.PATIENT_ID_FK),
                    result.getString(DaoConsts.PRESCRIPTION_DETAILS), 
                    result.getInt(DaoConsts.APPOINTMENT_ID_FK),
                    result.getDate(DaoConsts.PRESCRIPTION_ISSUE_DATE)
                );
                
                prescriptionList.add(prescription);
            }

            // close statement and result set
            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prescriptionList;
    }

    //For creating new prescription     
    public void addPrescription(int employeeId, int patientId, String prescriptionDetails, int appointmentId, Date issueDate) throws SQLException {

        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(INSERT_INTO_PRESCRIPTIONS);
        statement.setInt(1, employeeId);
        statement.setInt(2, patientId);
        statement.setString(3, prescriptionDetails);
        statement.setInt(4, appointmentId);
        statement.setDate(5, new java.sql.Date(issueDate.getTime()));
        statement.executeUpdate();

    }

}
