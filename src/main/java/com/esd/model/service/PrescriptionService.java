package com.esd.model.service;

import com.esd.model.dao.PrescriptionDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.PrescriptionRepeat;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.exceptions.InvalidIdValueException;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Prescription> getPrescriptionFromFilteredRequest(SystemUser currentUser, Map<String, Object> args)  {
        List<Prescription> prescriptionList = new ArrayList<>();
        try {
            prescriptionList = prescriptionDao.getFilteredDetails(currentUser, args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prescriptionList;
    }

    public synchronized static PrescriptionService getInstance(){
        if(instance == null){
            instance = new PrescriptionService();
        }
        return instance;
    }

    public void updatePrescription(Prescription prescription)  {
        try {
            prescriptionDao.updatePrescription(prescription);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
        }
    }

    public Prescription getPrescriptionForAppointment(int appointmentId) {
        return prescriptionDao.getMainPrescriptionForAppointment(appointmentId);
    }

    public Prescription getPrescriptionById(int appointmentId) {
        Prescription prescription = new Prescription();
        try {
            prescription = prescriptionDao.getPrescriptionById(appointmentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prescription;
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


    public List<Prescription> getChildPrescriptionsByPrescriptionId(int prescriptionId) {
        try {
            return prescriptionDao.getChildPrescriptionsByPrescriptionId(prescriptionId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
}
