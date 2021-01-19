package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                                      Date issueDate) throws SQLException, InvalidIdValueException {

        boolean employeeFound = userDetailsDao.validateUserDetailsExistByIdAndUserGroup(employeeDetailsId, UserGroup.DOCTOR, UserGroup.NURSE);
        boolean patientFound = userDetailsDao.validateUserDetailsExistByIdAndUserGroup(patientDetailsId, UserGroup.NHS_PATIENT, UserGroup.PRIVATE_PATIENT);

        Prescription prescription = new Prescription(
                employeeDetailsId,
                patientDetailsId,
                prescriptionDetails,
                appointmentId,
                issueDate);

        if (employeeFound && patientFound) {
            prescriptionDao.createPrescription(prescription);
            return true;
        }
        return false;
    }
    
    public List<Prescription> getPrescriptionFromFilteredRequest(Map<String, Object> args) throws SQLException {
        List<Prescription> prescriptionList = prescriptionDao.getFilteredDetails(args);
        return prescriptionList;
    }

    public synchronized static PrescriptionService getInstance(){
        if(instance == null){
            instance = new PrescriptionService();
        }
        return instance;
    }

    public void updatePrescription(Prescription prescription) throws InvalidIdValueException, SQLException {
        prescriptionDao.updatePrescription(prescription);
    }

    public Prescription getPrescriptionForAppointment(int appointmentId) {
        try {
            return prescriptionDao.getPrescriptionForAppointment(appointmentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
