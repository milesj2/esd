package com.esd.model.service;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.*;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, used to access appointments data objects
 */

public class AppointmentsService {

    private static AppointmentsService instance;
    private AppointmentDao appointmentDao = AppointmentDao.getInstance();

    private AppointmentsService(AppointmentDao appointmentDao){
        if(appointmentDao == null){
            throw new IllegalArgumentException("appointment dao must not be null");
        }
        this.appointmentDao = appointmentDao;
    }

    private boolean checkIfAptConflicts(Appointment appointment) throws SQLException {
        //check if there are conflicting appointments
        // todo update to include time and working day check
        List<Appointment> conflictingApts = appointmentDao.getAppointmentsInPeriodWithStatus(
                appointment.getAppointmentDate(),
                appointment.getAppointmentDate(),
                Optional.empty());
        if(conflictingApts.size() > 0){
            return false;
        }
        return true;
    }

    public synchronized static AppointmentsService getInstance(){
        if(instance == null){
            instance = new AppointmentsService(AppointmentDao.getInstance());
        }
        return instance;
    }

    public Appointment getAppointmentById(int AppointmentId) throws SQLException {
        return appointmentDao.getAppointmentById(AppointmentId);
    }

    public List<Appointment> getAppointmentsInRange(Date fromDate, Date toDate, Optional<Map<String, Object>> args) throws SQLException {
        return appointmentDao.getAppointmentsInPeriodWithArgs(fromDate, toDate, args);
    }

    public void createNewAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(!checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        appointmentDao.createAppointment(appointment);
    }

    public void updateAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(!checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        appointmentDao.updateAppointment(appointment);
    }
}
