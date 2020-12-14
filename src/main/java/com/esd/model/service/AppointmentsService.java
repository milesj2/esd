package com.esd.model.service;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.dao.ConnectionManager;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, used to access appointments data objects
 */

public class AppointmentsService {

    private static AppointmentsService instance;
    private AppointmentDao appointmentDao;

    private AppointmentsService(AppointmentDao appointmentDao){
        if(appointmentDao == null){
            throw new IllegalArgumentException("appointment dao must not be null");
        }
        this.appointmentDao = appointmentDao;
    }

    public synchronized static AppointmentsService getInstance(){
        if(instance == null){
            instance = new AppointmentsService(AppointmentDao.getInstance());
        }
        return instance;
    }

    public Appointment getAppointmentById(int AppoinmentId) throws SQLException {
        return appointmentDao.getInstance().getAppointmentById(AppoinmentId);
    }

    public List<Appointment> getAppointmentsInRange(Date fromDate, Date toDate, Map<String, String> args) throws SQLException {
        return appointmentDao.getInstance().getAppointmentsInPeriodWithArgs(fromDate, toDate, args);
    }

    public void createNewAppointment(Appointment appointment) throws SQLException {
        appointmentDao.getInstance().createAppointment(appointment);
    }

    public void updateAppointmentById(Appointment appointment) throws SQLException {
        appointmentDao.getInstance().updateAppointment(appointment);
    }
}
