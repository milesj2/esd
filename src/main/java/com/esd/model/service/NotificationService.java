package com.esd.model.service;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is to do any functionality needed for dashboard notifications
 */
public class NotificationService {
    private static NotificationService instance;
    private AppointmentService appointmentService;
    private InvoiceService invoiceService;

    private NotificationService(AppointmentService appointmentService, InvoiceService invoiceService){
        if (appointmentService == null) {
            throw new IllegalArgumentException("appointmentService must not be null");
        }
        if (invoiceService == null) {
            throw new IllegalArgumentException("invoiceService must not be null");
        }
        this.appointmentService = appointmentService;
        this.invoiceService = invoiceService;
    }

    public synchronized static NotificationService getInstance() {
        if (instance == null){
            instance = new NotificationService(AppointmentService.getInstance(), InvoiceService.getInstance());
        }
        return instance;
    }
}
