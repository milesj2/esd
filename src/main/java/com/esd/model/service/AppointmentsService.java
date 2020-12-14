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

    public void updateAppointmentInstanceById(int AppointmentId, HttpServletRequest request) throws SQLException {
        appointmentDao.getInstance().updateAppointmentById(AppointmentId, request);
    }

    public void cancelAppointmentById(int AppointmentId) throws SQLException {
        appointmentDao.getInstance().cancelAppointmentById(AppointmentId);
    }

    public void createNewAppointment(HttpServletRequest request) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID)));
        appointment.setAppointmentDate(Date.from(Instant.parse(request.getParameter(DaoConsts.APPOINTMENT_DATE))));
        appointment.setAppointmentTime(Date.from(Instant.parse(request.getParameter(DaoConsts.APPOINTMENT_TIME))));
        appointment.setSlots(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
        appointment.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
        appointment.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
        appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(DaoConsts.APPOINTMENT_STATUS)));

        appointmentDao.getInstance().createAppointmentFromRequest(appointment);
    }

    public List<Appointment> getAppointmentsInRange(Date fromDate, Date toDate) throws SQLException {
        return appointmentDao.getInstance().getAppointmentsInPeriodWithStatus(fromDate, toDate, Optional.empty());
    }
}
