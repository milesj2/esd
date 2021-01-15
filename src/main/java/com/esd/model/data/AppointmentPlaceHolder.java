package com.esd.model.data;

import com.esd.model.data.persisted.Appointment;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class AppointmentPlaceHolder {

    private int employeeId;
    private int slots = 1;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public AppointmentPlaceHolder(int employeeId, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.employeeId = employeeId;
        this.slots = slots;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public AppointmentPlaceHolder(int employeeId, int slots, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.employeeId = employeeId;
        this.slots = slots;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getSlots() {
        return slots;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }
}
