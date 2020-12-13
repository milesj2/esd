package com.esd.model.service;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<Appointment> getAppointments(ArrayList<String> formKeys, HttpServletRequest request) throws SQLException {
        return appointmentDao.getInstance().getFilteredAppointmentsFromRequest(formKeys,request);

    }

    public void updateAppointmentInstanceById(int AppointmentId, HttpServletRequest request) {
        appointmentDao.getInstance().updateAppointmentById(AppointmentId, request);
    }

    public void cancelAppointmentById(int AppointmentId) throws SQLException {
        appointmentDao.getInstance().cancelAppointmentById(AppointmentId);
    }
}
