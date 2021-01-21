package com.esd.model.data.persisted;

import com.esd.model.data.AppointmentStatus;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.SystemSettingService;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about an appointment
 */
public class Appointment {
    private int id;
    private int patientId;
    private int employeeId;
    private int slots = 1;
    private int thirdPartyId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private String notes;
    private UserDetails patientDetails;
    private UserDetails employeeDetails;

    public Appointment() {
    }

    public Appointment(LocalDate date, LocalTime withMinuteOfHour, AppointmentStatus pending) {
        this.appointmentDate = date;
        this.appointmentTime = withMinuteOfHour;
        this.status = pending;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public boolean isCanceled() {
       return this.status.equals(AppointmentStatus.CANCELED);
    }

    public int getThirdPartyId() { return thirdPartyId; }

    public void setThirdPartyId(int thirdPartyId) { this.thirdPartyId = thirdPartyId; }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(UserDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public UserDetails getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(UserDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public LocalTime getEndTime() {
        try {
            int slotTime = SystemSettingService.getInstance().getIntegerSettingValueByKey(SystemSettingService.SYSTEMSETTING_SLOT_TIME);
            return getAppointmentTime().plusMinutes(slotTime * getSlots());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
        }
        return getAppointmentTime();
    }
}
