package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
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

/**
 * Original Author: Trent Meier
 * Use: Dao class to handle CRU(d) operations for Appointments
 */

public class AppointmentDao {

    private static AppointmentDao instance;

    private static String INSERT_APPOINTMENT = "insert into appointments " +
            "(id, appointmentdate, appointmenttime, slots, employeeid, patientid, appointmentstatus)" +
            " values (?,?,?,?,?,?,?)";

    private static String UPDATE_APPOINTMENT = "update appointments set values" +
            "(id, appointmentdate, appointmenttime, slots, employeeid, patientid, appointmentstatus)" +
            " values (?,?,?,?,?,?) where id = ?";

    private static String SELECT_APPOINTMENTS_WITHIN_RANGE =  "select * from appointments" +
            " where appointmentDate >= ? and appointmentDate <= ? ";

    private static String SELECT_APPOINTMENT = "select * from appointments where id = ?";

    private Appointment processResultSetForAppointment(ResultSet resultSet) throws SQLException {
        Appointment appointment =  new Appointment();
        appointment.setId(resultSet.getInt(DaoConsts.ID));
        appointment.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID_FK));
        appointment.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID_FK));
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

    public void updateAppointment(Appointment appointment) throws SQLException {
        if(appointment.getId()==0){
            throw new IllegalArgumentException("appointment must have id");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_APPOINTMENT);
        statement.setDate(1, (java.sql.Date) appointment.getAppointmentDate());
        statement.setTime(2, (Time) appointment.getAppointmentTime());
        statement.setInt(3, appointment.getSlots());
        statement.setInt(4, appointment.getEmployeeId());
        statement.setInt(5, appointment.getPatientId());
        statement.setString(6, appointment.getStatus().toString());
        //where id
        statement.setInt(7, appointment.getId());
        statement.executeQuery();
    }

    public void createAppointment(Appointment appointment) throws SQLException {
        if(appointment.getId()!=0){
            throw new IllegalArgumentException("new appointment cannot have prepopulate id");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT_APPOINTMENT);
        statement.setInt(1, appointment.getId());
        statement.setDate(2, (java.sql.Date) appointment.getAppointmentDate());
        statement.setTime(3, (Time) appointment.getAppointmentTime());
        statement.setInt(4, appointment.getSlots());
        statement.setInt(5, appointment.getEmployeeId());
        statement.setInt(6, appointment.getPatientId());
        statement.setString(7, appointment.getStatus().toString());
        statement.executeQuery();
    }

    public List<Appointment> getAppointmentsInPeriodWithArgs(Date start, Date end, Map<String, String> args)
            throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        String query = SELECT_APPOINTMENTS_WITHIN_RANGE;

        //iterate and build query
        Iterator it = args.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            query += " and " + pair.getKey() + " = ?";
        }

        PreparedStatement statement = con.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(start.getTime()));
        statement.setDate(2, new java.sql.Date(end.getTime()));

        int NextStatementIndex  = 3;
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            statement.setString(NextStatementIndex, (String) pair.getValue());
            NextStatementIndex++;
        }

        ResultSet result = statement.executeQuery();
        List<Appointment> appointments = new ArrayList<>();

        while (result.next()){
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments;
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(SELECT_APPOINTMENT);
        statement.setInt(1, appointmentId);
        ResultSet result = statement.executeQuery();
        if(!result.next()){
            throw new SQLDataException("No result exists for Appointment id: " + appointmentId);
        }
        return processResultSetForAppointment(result);
    }
}
