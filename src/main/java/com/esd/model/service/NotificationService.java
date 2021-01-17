package com.esd.model.service;

import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;

import java.sql.SQLException;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is to do any functionality needed for dashboard notifications
 */
public class NotificationService {
    private static NotificationService instance;
    private AppointmentsService appointmentsService;
    private InvoiceService invoiceService;

    public String getLastAddedAppointmentInfo() throws SQLException {
        Appointment app = appointmentsService.getLastAddedAppointment();
        return app == null
                ? "No appointments added yet."
                : getAppointmentStrInfo(app);
    }

    public String getLastAddedInvoiceInfo() throws SQLException {
        Invoice inv = invoiceService.getLastAddedInvoice();
        if (inv == null) {
            return "No invoices added yet.";
        }
        return "ID: " + inv.getId()
                + "<br>Date: " + inv.getInvoiceDate() + ", " + inv.getInvoiceTime()
                + "<br>Patient ID: " + inv.getPatientId()
                + "<br>Employee ID: " + inv.getEmployeeId()
                + "<br>Appointment ID: " + inv.getAppointmentId();
    }

    public String getNextAppointment(int patientId) throws SQLException {
        Appointment app = appointmentsService.getNextAppointment(patientId);
        return app == null
                ? "No pending appointments yet."
                : getAppointmentStrInfo(app);
    }

    public String getAppointmentStrInfo(Appointment app) {
        return  "ID: " + app.getId()
                + "<br>Date: " + app.getAppointmentDate() + ", " + app.getAppointmentTime()
                + "<br>Status: " + app.getStatus().toString()
                + "<br>Patient ID: " + app.getPatientId()
                + "<br>Employee ID: " + app.getEmployeeId();
    }

    private NotificationService(AppointmentsService appointmentService, InvoiceService invoiceService){
        if (appointmentService == null) {
            throw new IllegalArgumentException("appointmentService cannot be null!");
        }
        if (invoiceService == null) {
            throw new IllegalArgumentException("invoiceService cannot be null!");
        }
        this.appointmentsService = appointmentsService;
        this.invoiceService = invoiceService;
    }

    public synchronized static NotificationService getInstance() {
        if (instance == null){
            instance = new NotificationService(AppointmentsService.getInstance(), InvoiceService.getInstance());
        }
        return instance;
    }
}
