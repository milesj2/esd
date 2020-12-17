package com.esd.model.data.persisted;

import com.esd.model.data.AppointmentStatus;

import java.util.Date;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about an appointment
 */
public class Appointment {
    private int id;
    private int patientId;
    private int employeeId;
    private int slots;
    private Date appointmentDate;
    private Date appointmentTime; //Could use the Joda time libary which is a little nicer....
    private AppointmentStatus status;

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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
