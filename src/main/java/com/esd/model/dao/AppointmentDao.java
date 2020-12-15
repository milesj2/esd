package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.persisted.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AppointmentDao {

    private static AppointmentDao instance;

    public List<Appointment> getAppointmentsInPeriodWithStatus(Date start, Date end, Optional<AppointmentStatus> status) throws SQLException {
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
