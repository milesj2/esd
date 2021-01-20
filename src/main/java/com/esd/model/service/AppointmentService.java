package com.esd.model.service;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.exceptions.InvalidIdValueException;

import org.joda.time.LocalDate;
import java.sql.SQLException;
import java.util.*;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, used to access appointments data objects
 */

public class AppointmentService {

    private static AppointmentService instance;
    private AppointmentDao appointmentDao = AppointmentDao.getInstance();

    private AppointmentService(AppointmentDao appointmentDao){
        if(appointmentDao == null){
            throw new IllegalArgumentException("appointmentDao cannot be null!");
        }
        this.appointmentDao = appointmentDao;
    }

    private boolean checkIfAptConflicts(Appointment appointment) throws SQLException {
        //check if there are conflicting appointments
        List<Appointment> conflictingApts = appointmentDao.getAppointmentsInPeriodWithStatus(
                appointment.getAppointmentDate(),
                appointment.getAppointmentDate(),
                Optional.empty());
        if(conflictingApts.size() > 0){
            return false;
        }
        return true;
    }

    private boolean checkIfAptIsWorkingDay(Appointment appointment) {
        //joda time day of week 6 or 7 is weekend (non working day)
        int dayOfWeek = appointment.getAppointmentDate().getDayOfWeek();
        if(dayOfWeek == 6 || dayOfWeek == 7){
            return false;
        }
        return true;
    }

    public synchronized static AppointmentService getInstance(){
        if(instance == null){
            instance = new AppointmentService(AppointmentDao.getInstance());
        }
        return instance;
    }

    public Appointment getAppointmentById(int AppointmentId) throws SQLException {
        return appointmentDao.getAppointmentById(AppointmentId);
    }

    public List<Appointment> getAppointmentsInRange(LocalDate fromDate, LocalDate toDate, Optional<Map<String, Object>> args) throws SQLException {
        return appointmentDao.getAppointmentsInPeriodWithArgs(fromDate, toDate, args);
    }

    public Appointment getLastAddedAppointment() throws SQLException {
        return appointmentDao.getAllAppointments().stream()
                .max(Comparator.comparing(Appointment::getId))
                .orElse(null);
    }

    public Appointment getNextPendingAppointment(int patientId) throws SQLException {
        List<Appointment> appointments = appointmentDao.getAllAppointments();
        if (appointments.isEmpty()) {
            return null;
        }
        return appointments.stream()
                .filter(app -> app.getPatientId() == patientId && AppointmentStatus.PENDING.equals(app.getStatus()))
                .reduce((app1, app2)
                        -> app1.getAppointmentDate().getDayOfYear() < app2.getAppointmentDate().getDayOfYear()
                            && app1.getAppointmentDate().getYear() < app2.getAppointmentDate().getYear()
                        ? app1
                        : app2)
                .orElse(null);
    }

    public void createNewAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(!checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        if(!checkIfAptIsWorkingDay(appointment)){
            throw new InvalidIdValueException("Appointment cannot be for a non-working day");
        }
        appointmentDao.createAppointment(appointment);
    }

    public void updateAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        if(!checkIfAptIsWorkingDay(appointment)){
            throw new InvalidIdValueException("Appointment cannot be for a non-working day");
        }
        appointmentDao.updateAppointment(appointment);
    }
}
