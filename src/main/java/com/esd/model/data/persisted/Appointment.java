package com.esd.model.data.persisted;

import com.esd.model.data.AppointmentStatus;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about an appointment
 */
public class Appointment {
    private int id;
    private int patientId;
    private int employeeId;
    private int slots = 1;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalTime appointmentEndTime;
    private AppointmentStatus status;

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

    public boolean isCancled() {
       return this.status.equals(AppointmentStatus.CANCELED);
    }
}
