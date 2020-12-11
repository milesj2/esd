package com.esd.model.dao;

import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AppointmentDao {

    private static AppointmentDao instance;


    private static String LOADALLINVOICEWITHSTATUSRESTRICTION = "select * from appointments\n" +
            "    where  appointmentStatus = ?";

    private static String APPOINTMENT_STATUS_RESTRICTION =   "AND appointmentStatus = ?";

    private static String LOADAPPOINTMENTSINPERIODWITHSTATUS =  "select * from appointments\n" +
            "    where appointmentDate >= ? AND appointmentDate <= ? ";

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
}
