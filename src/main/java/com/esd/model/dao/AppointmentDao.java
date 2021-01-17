package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class AppointmentDao {

    private static AppointmentDao instance;

    private static String INSERT_APPOINTMENT = "insert into appointments " +
            "(appointmentdate, appointmenttime, slots, employeeid, patientid, appointmentstatus, notes)" +
            " values (?,?,?,?,?,?,?)";

    private static String UPDATE_APPOINTMENT = "update appointments set" +
            " appointmentdate = ?," +
            " appointmenttime = ?," +
            " slots = ?," +
            " employeeid = ?," +
            " patientid = ?," +
            " appointmentstatus = ?, " +
            " notes = ? " +
            "where id = ?";

    public void updateAppointment(Appointment appointment) throws SQLException {
        if(appointment.getId()==0){
            throw new IllegalArgumentException("appointment must have id");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(UPDATE_APPOINTMENT);
        statement.setDate(1, Date.valueOf(appointment.getAppointmentDate().toString()));
        statement.setTime(2, new Time(appointment.getAppointmentTime().toDateTimeToday().getMillis()));
        statement.setInt(3, appointment.getSlots());
        statement.setInt(4, appointment.getEmployeeId());
        statement.setInt(5, appointment.getPatientId());
        statement.setString(6, appointment.getStatus().toString());
        statement.setString(7, appointment.getNotes());
        //where id
        statement.setInt(8, appointment.getId());
        statement.executeUpdate();
    }

    public void createAppointment(Appointment appointment) throws SQLException {
        if(appointment.getId()!=0){
            throw new IllegalArgumentException("new appointment cannot have prepopulate id");
        }
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT_APPOINTMENT);
        statement.setDate(1, Date.valueOf(appointment.getAppointmentDate().toString()));
        statement.setTime(2, new Time(appointment.getAppointmentTime().toDateTimeToday().getMillis()));
        statement.setInt(3, appointment.getSlots());
        statement.setInt(4, appointment.getEmployeeId());
        statement.setInt(5, appointment.getPatientId());
        statement.setString(6, appointment.getStatus().toString());
        statement.setString(7, appointment.getNotes());
        statement.executeUpdate();
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
        appointment.setNotes(resultSet.getString(DaoConsts.APPOINTMENT_NOTES));
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

    public Appointment getLastAddedAppointment() throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS);

        ResultSet result = queryBuilder.createStatement().executeQuery();
        List<Appointment> appointments = new ArrayList<>();

        while (result.next()) {
            appointments.add(processResultSetForAppointment(result));
        }
        return appointments.stream()
                .max(Comparator.comparing(Appointment::getId))
                .orElse(null);
    }

    public List<Appointment> getPatientAppointmentsById(int patientId) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS);
        queryBuilder.and(Restrictions.equalsRestriction(DaoConsts.PATIENT_ID, patientId));
        queryBuilder.and(Restrictions.equalsRestriction(DaoConsts.APPOINTMENT_STATUS, AppointmentStatus.PENDING));

        ResultSet result = queryBuilder.createStatement().executeQuery();
        List<Appointment> appointments = new ArrayList<>();

        while (result.next()) {
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

    public ArrayList<Appointment> getAppointmentsByFilteredResults(SystemUser currentUser, Map<String, Object> args) throws SQLException {
        ArrayList<Appointment> appointmentsList = new ArrayList<>();
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_APPOINTMENTS);

        Iterator mapIter = args.entrySet().iterator();
        while(mapIter.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIter.next();
            queryBuilder.and(Restrictions.like(pair.getKey().toString(), pair.getValue()));
        }

        switch(currentUser.getUserGroup()){
            case PRIVATE_PATIENT:
            case NHS_PATIENT:
                queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.PATIENT_ID_FK, currentUser.getUserDetails().getId()));
                break;
        }

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();

        while (result.next()){
            appointmentsList.add(processResultSetForAppointment(result));
        }
        return appointmentsList;
    }
}
