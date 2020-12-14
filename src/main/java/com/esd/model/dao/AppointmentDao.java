package com.esd.model.dao;

import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.data.persisted.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class AppointmentDao {

    private static AppointmentDao instance;

    private static String APPOINTMENT_STATUS_RESTRICTION =   "AND appointmentStatus = ?";
    private static String LOADAPPOINTMENTSINPERIODWITHSTATUS =  "select * from appointments\n" +
            "    where appointmentDate >= ? AND appointmentDate <= ? ";
    private static String SELECT_APPOINTMENT = "select * from appointments where id = ?";
    private static String UPDATE_SETCANCELLED = "update appointments set appointmentstatus = 'CANCELED' where id = ?";
    private static String UPDATE_BY_REQUEST = "update appointments set ";
    private static String INSERT_APPOINTMENT = "insert into appointments " +
            "(id, appointmentdate, appointmenttime, slots, employeeid, patientid, appointmentstatus)" +
            " values (?,?,?,?,?,?,?)";

    public List<Appointment> getAppointmentsInPeriodWithStatus(Date start, Date end, Optional<AppointmentStatus> status) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        String query = LOADAPPOINTMENTSINPERIODWITHSTATUS;
        if(status.isPresent()){
            query += APPOINTMENT_STATUS_RESTRICTION;
        }
        PreparedStatement statement = con.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(start.getTime()));
        statement.setDate(2, new java.sql.Date(start.getTime()));
        if(status.isPresent()){
            statement.setString(3, status.get().name());
        }

        ResultSet result = statement.executeQuery();
        List<Appointment> appointments = new ArrayList<>();

        while (result.next()){
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments;
    }

    private Appointment processResultSetForAppointment(ResultSet resultSet) throws SQLException {
        Appointment appointment =  new Appointment();
        appointment.setId(resultSet.getInt(DaoConsts.ID));
        appointment.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID));
        appointment.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID));
        appointment.setAppointmentDate(resultSet.getDate(DaoConsts.APPOINTMENT_DATE));
        appointment.setAppointmentTime(resultSet.getTime(DaoConsts.APPOINTMENT_TIME));
        appointment.setSlots(resultSet.getInt(DaoConsts.APPOINTMENT_SLOTS));
        appointment.setStatus(AppointmentStatus.valueOf(resultSet.getString(DaoConsts.APPOINTMENT_STATUS)));
        return appointment;
    }

    public synchronized static AppointmentDao getInstance(){
        if(instance == null){
            instance = new AppointmentDao();
        }
        return instance;
    }

    public Appointment getAppointmentById(int appoinmentId) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(SELECT_APPOINTMENT);
        statement.setInt(1, appoinmentId);
        ResultSet result = statement.executeQuery();
        if(!result.next()){
            //todo some error
        }
        return processResultSetForAppointment(result);
    }

    public void updateAppointmentById(int appointmentId, HttpServletRequest request) throws SQLException {
        Enumeration<String> params = request.getParameterNames();
        String UPDATE_APPOINTMENT = UPDATE_BY_REQUEST;

        boolean first = true;
        while(params.hasMoreElements()){
            String key = params.nextElement();
            if(first){
                if(!request.getParameter(key).isEmpty()) {
                    UPDATE_APPOINTMENT += key + " = ? ";
                }
                first = false;
            } else {
                if(!request.getParameter(key).isEmpty()) {
                    UPDATE_APPOINTMENT += ", " + key + " = ? ";
                }
            }
        }

        UPDATE_APPOINTMENT += " where id = ?";

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_APPOINTMENT);

        int i = 1;
        while(params.hasMoreElements()){
            String key = params.nextElement();
            if(!request.getParameter(key).isEmpty()) {
                statement.setString(i, request.getParameter(key));
                i++;
            }
        }

        statement.setInt(i, appointmentId);
        statement.executeUpdate();
    }

    public void cancelAppointmentById(int appointmentId) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_SETCANCELLED);
        statement.setInt(1, appointmentId);
        statement.executeUpdate();
    }

    public void createAppointmentFromRequest(Appointment appointment) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT_APPOINTMENT);
        statement.setInt(1, appointment.getId());
        statement.setDate(2, (java.sql.Date) appointment.getAppointmentDate());
        statement.setTime(3, (Time) appointment.getAppointmentTime());
        statement.setInt(4, appointment.getSlots());
        statement.setInt(5, appointment.getEmployeeId());
        statement.setInt(6, appointment.getPatientId());
        statement.setString(6, appointment.getStatus().toString());
    }
}
