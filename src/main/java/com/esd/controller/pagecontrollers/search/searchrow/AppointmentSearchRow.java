package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;

import java.util.Arrays;
import java.util.HashMap;

public class AppointmentSearchRow extends SearchRow{

    public AppointmentSearchRow(Appointment appointment) {
        this.id = appointment.getId();
        this.columns = Arrays.asList(
                String.valueOf(appointment.getId()),
                String.valueOf(appointment.getEmployeeId()),
                String.valueOf(appointment.getPatientId()),
                String.valueOf(appointment.getSlots()),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentTime().toString(),
                appointment.getStatus().toString()
        );
        searchActions = new HashMap<>();
        searchActions.put("/appointments/view?selectedAppointmentId="+appointment.getId(), "View Appointment Details");
    }
}
