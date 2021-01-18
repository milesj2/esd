package com.esd.model.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class UIAppointment {
    private int id;
    private String title;
    private int slots;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}