package com.esd.model.service;

import com.esd.model.data.QuickNotification;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is to do any functionality needed for dashboard notifications
 */
public class NotificationService {

    private static NotificationService instance;
    private AppointmentsService appointmentsService;
    private InvoiceService invoiceService;

    public List<QuickNotification> createQuickNotifications(SystemUser currentSystemUser) throws InvalidIdValueException, SQLException {
        UserGroup userGroup = currentSystemUser.getUserGroup();
        UserDetails userDetails = UserDetailsService.getInstance().getUserDetailsByUserID(currentSystemUser.getId());

        List<QuickNotification> quickNotifications = new ArrayList<>();

        if (userGroup.equals(UserGroup.NHS_PATIENT) || userGroup.equals(UserGroup.PRIVATE_PATIENT)) {
            QuickNotification nextPendingApp = new QuickNotification("Your next pending appointment:",
                            getNextPendingAppointment(userDetails.getUserId()));

            quickNotifications.add(nextPendingApp);
        } else {
            QuickNotification lastAddedApp = new QuickNotification("Last added appointment:",
                    getLastAddedAppointmentInfo());
            QuickNotification lastAddedInv = new QuickNotification("Last added invoice:",
                    getLastAddedInvoiceInfo());

            quickNotifications.add(lastAddedApp);
            quickNotifications.add(lastAddedInv);
        }

        return quickNotifications;
    }

    public List<String> getNextPendingAppointment(int patientId) throws SQLException {
        Appointment app = appointmentsService.getNextPendingAppointment(patientId);
        return app == null
                ? Arrays.asList("No pending appointments yet.")
                : getAppointmentStrInfo(app);
    }

    public List<String> getLastAddedAppointmentInfo() throws SQLException {
        Appointment app = appointmentsService.getLastAddedAppointment();
        return app == null
                ? Arrays.asList("No appointments added yet.")
                : getAppointmentStrInfo(app);
    }

    public List<String> getLastAddedInvoiceInfo() throws SQLException {
        Invoice inv = invoiceService.getLastAddedInvoice();
        return inv == null
                ? Arrays.asList("No invoices added yet.")
                : Arrays.asList("ID: " + inv.getId(),
                    "Date: " + inv.getInvoiceDate() + ", " + inv.getInvoiceTime(),
                    "Patient ID: " + inv.getPatientId(),
                    "Employee ID: " + inv.getEmployeeId(),
                    "Appointment ID: " + inv.getAppointmentId());
    }

    public List<String> getAppointmentStrInfo(Appointment app) {
        return Arrays.asList("ID: " + app.getId(),
                "Date: " + app.getAppointmentDate() + ", " + app.getAppointmentTime(),
                "Status: " + app.getStatus().toString(),
                "Patient ID: " + app.getPatientId(),
                "Employee ID: " + app.getEmployeeId());
    }

    private NotificationService(AppointmentsService appointmentsService, InvoiceService invoiceService){
        if (appointmentsService == null) {
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
