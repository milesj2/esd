package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.PrescriptionRepeat;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.exceptions.InvalidIdValueException;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public boolean createPrescription(Prescription prescription) {
        try {
            prescriptionDao.createPrescription(prescription);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
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

    public void repeatPrescription(Prescription prescription, PrescriptionRepeat repeat, LocalDate repeatUntil) {
        if(repeat == PrescriptionRepeat.NEVER){
            return;
        }


        int originatingPrescriptionId = prescription.getId();

        if(repeat == PrescriptionRepeat.WEEKLY){
            LocalDate newDate = new LocalDate(prescription.getIssueDate());
            while((newDate = newDate.plusWeeks(1)).isBefore(repeatUntil) || newDate.isEqual(repeatUntil)){
                prescription.setId(0);
                prescription.setOriginatingPrescriptionId(originatingPrescriptionId);
                prescription.setIssueDate(newDate);
                createPrescription(prescription);
            }
        }

        if(repeat == PrescriptionRepeat.MONTHLY){
            LocalDate newDate = new LocalDate(prescription.getIssueDate());
            while((newDate = newDate.plusMonths(1)).isBefore(repeatUntil) || newDate.isEqual(repeatUntil)){
                prescription.setId(0);
                prescription.setOriginatingPrescriptionId(originatingPrescriptionId);
                prescription.setIssueDate(newDate);
                createPrescription(prescription);
            }
        }
    }
}
