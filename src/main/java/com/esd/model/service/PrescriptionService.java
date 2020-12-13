package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;
import com.esd.model.dao.UserDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, used to access prescription data objs
 */

public class PrescriptionService {
    
    private static PrescriptionService instance;
    private PrescriptionDao prescriptionDao = PrescriptionDao.getInstance();
    private UserDetailsDao userDetailsDao = UserDetailsDao.getInstance();

    private PrescriptionService() {
    }

    public boolean createPrescription(int employeeDetailsId, int patientDetailsId, String prescriptionDetails, int appointmentId,
                                      Date issueDate) throws SQLException {

        boolean employeeFound = userDetailsDao.validateUserDetailsExistByIdAndUserGroup(employeeDetailsId, UserGroup.DOCTOR, UserGroup.NURSE);
        boolean patientFound = userDetailsDao.validateUserDetailsExistByIdAndUserGroup(patientDetailsId, UserGroup.PATIENT);
        
        if (employeeFound && patientFound) {
            prescriptionDao.addPrescription(employeeDetailsId, patientDetailsId, prescriptionDetails, appointmentId, issueDate);
            return true;
        }
        return false;
    }
    
    public static ArrayList<Prescription> getPrescriptionFromFilteredRequest(ArrayList<String> formKeys,
                                                                           HttpServletRequest request) {
        ArrayList<Prescription> prescription = PrescriptionDao.getInstance().getFilteredDetails(formKeys, request);
        return prescription;
    }

    public synchronized static PrescriptionService getInstance(){
        if(instance == null){
            instance = new PrescriptionService();
        }
        return instance;
    }
}
