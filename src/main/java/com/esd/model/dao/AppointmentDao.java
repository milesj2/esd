package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class AppointmentDao {

    private static AppointmentDao instance;

    private static String INSERT_APPOINTMENT = "insert into appointments " +
            "(id, appointmentdate, appointmenttime, slots, employeeid, patientid, appointmentstatus)" +
            " values (?,?,?,?,?,?,?)";
    private static String UPDATE_APPOINTMENT = "update appointments set" +
            " id = ?," +
            " appointmentdate = ?," +
            " appointmenttime = ?," +
            " slots = ?," +
            " employeeid = ?," +
            " patientid = ?," +
            " appointmentstatus = ? " +
            "where id = ?";
    private static String GET_PATIENT_APPOINTMENTS = "SELECT * FROM appointments WHERE appointmentdate>=? AND appointmentdate<=? AND patientID=?";
    private static String GET_EMPLOYEE_APPOINTMENTS = "SELECT * FROM appointments WHERE appointmentdate>=? AND appointmentdate<=? AND employeeID=?";


    public void updateAppointment(Appointment appointment) throws SQLException {
        if(appointment.getId()==0){
            throw new IllegalArgumentException("appointment must have id");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_APPOINTMENT);
        statement.setDate(1, Date.valueOf(appointment.getAppointmentDate().toString()));
        statement.setTime(2, Time.valueOf(appointment.getAppointmentTime().toString()));
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
        statement.setDate(2, Date.valueOf(appointment.getAppointmentDate().toString()));
        statement.setTime(3, Time.valueOf(appointment.getAppointmentTime().toString()));
        statement.setInt(4, appointment.getSlots());
        statement.setInt(5, appointment.getEmployeeId());
        statement.setInt(6, appointment.getPatientId());
        statement.setString(7, appointment.getStatus().toString());
        statement.executeQuery();
    }

    public List<Appointment> getAppointmentsInPeriodWithStatus(LocalDate start, LocalDate end, Optional<AppointmentStatus> status) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS)
                .and(Restrictions.greaterThanInclusive(DaoConsts.APPOINTMENT_DATE, start))
                .and(Restrictions.lessThanInclusive(DaoConsts.APPOINTMENT_DATE, end));

        if(status.isPresent()){
            queryBuilder.and(Restrictions.equalsRestriction(DaoConsts.APPOINTMENT_STATUS, status.get()));
        }

        ResultSet result = queryBuilder.createStatement().executeQuery();
        List<Appointment> appointments = new ArrayList<>();

        while (result.next()){
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments;
    }

    private Appointment processResultSetForAppointment(ResultSet resultSet) throws SQLException {
        Appointment appointment =  new Appointment();
        appointment.setId(resultSet.getInt(DaoConsts.ID));
        appointment.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID_FK));
        appointment.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID_FK));
        appointment.setAppointmentDate(LocalDate.parse(resultSet.getString(DaoConsts.APPOINTMENT_DATE)));
        appointment.setAppointmentTime(LocalTime.parse(resultSet.getString(DaoConsts.APPOINTMENT_TIME)));
        appointment.setSlots(resultSet.getInt(DaoConsts.APPOINTMENT_SLOTS));
        appointment.setStatus(AppointmentStatus.valueOf(resultSet.getString(DaoConsts.APPOINTMENT_STATUS)));
        return appointment;
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS);
        queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, appointmentId));
        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();
        if(!result.next()){
            throw new SQLDataException("No result exists for Appointment id: " + appointmentId);
        }
        return processResultSetForAppointment(result);
    }

    public List<Appointment> getPatientAppointments(LocalDate start, LocalDate end, int patientID) throws SQLException {
        return getAppointments(start, end, patientID, false);
    }

    public List<Appointment> getEmployeeAppointments(LocalDate start, LocalDate end, int employeeID) throws SQLException {
        return getAppointments(start, end, employeeID, true);
    }

    private List<Appointment> getAppointments(LocalDate start, LocalDate end, int userID, boolean employee) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement;
        if (employee) {
            statement = con.prepareStatement(GET_EMPLOYEE_APPOINTMENTS);
        } else {
            statement = con.prepareStatement(GET_PATIENT_APPOINTMENTS);
        }

        statement.setString(1, start.toString());
        statement.setString(2, end.toString());
        statement.setInt(3, userID);

        ResultSet result = statement.executeQuery();

        while (result.next()){
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments;
    }

    public List<Appointment> getAppointmentsInPeriodWithArgs(LocalDate start, LocalDate end,  Optional<Map<String, Object>> args)
            throws SQLException {
        List<Appointment> appointments = new ArrayList<>();

        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS);
        queryBuilder.and(Restrictions.greaterThanInclusive(DaoConsts.APPOINTMENT_DATE, start))
                .and(Restrictions.lessThanInclusive(DaoConsts.APPOINTMENT_DATE, end));

        if(args.isPresent()){
            Iterator mapIter = args.get().entrySet().iterator();
            while(mapIter.hasNext()) {
                Map.Entry pair = (Map.Entry)mapIter.next();
                queryBuilder.and(Restrictions.equalsRestriction(pair.getKey().toString(), pair.getValue()));
            }
        }

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();

        while (result.next()){
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments;
    }

    public synchronized static AppointmentDao getInstance(){
        if(instance == null){
            instance = new AppointmentDao();
        }
        return instance;
    }
}
