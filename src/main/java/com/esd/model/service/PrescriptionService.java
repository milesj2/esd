package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.Prescription;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 * Original Author: Angela Jackson
 * Use: This class is a singleton, used to access prescription data objs
 */

public class PrescriptionService {
    
    private static PrescriptionService instance;
    private PrescriptionDao prescriptionDao;

    private PrescriptionService(PrescriptionDao prescriptionDao) {
        if(prescriptionDao == null){
            throw new IllegalArgumentException("prescriptionDao must not be null");
        }
        this.prescriptionDao = prescriptionDao;
    }

    public synchronized static PrescriptionService getInstance(){
        if(instance == null){
            instance = new PrescriptionService(PrescriptionDao.getInstance());
        }
        return instance;
    }
    

    public boolean createPrescription(String doctorFirstName, String doctorLastName, String patientFirstName, String patientLastName,
            String prescriptionDetails, String appointmentId, String issueDate) 
        throws SQLException {
        
        //check if doctor and patient exsists with the names provided and get id.
        UserDetailsDao db = UserDetailsDao.getInstance();
        boolean doctorFound = db.verifyUserMatch(doctorFirstName, doctorLastName);
        boolean patientFound = db.verifyUserMatch(patientFirstName, patientLastName);
        
        if (doctorFound && patientFound) {
        
            String doctorId =  db.getUserId(doctorFirstName, doctorLastName); //get doctote id
            String patientId =  db.getUserId(patientFirstName, patientLastName); //get patient id

            PrescriptionDao db2 = PrescriptionDao.getInstance();
            db2.addPrescription(doctorId, patientId, prescriptionDetails, appointmentId, issueDate);
            return true;
        }
        return false;

    }
    
    public static ArrayList<Prescription> getPrescriptionFromFilteredRequest(ArrayList<String> formKeys,
                                                                           HttpServletRequest request) {
        ArrayList<Prescription> prescription = PrescriptionDao.getInstance().getFilteredDetails(formKeys, request);
        return prescription;
    }

}
