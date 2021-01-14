package com.esd.model.service;

/**
 * Original Author: Sam Barba
 * Use: This class is a singleton, the use of which is to do any functionality needed for dashboard notifications
 */
public class NotificationService {

    private static NotificationService instance;
    private AppointmentsService appointmentsService;
    private InvoiceService invoiceService;

    private NotificationService(AppointmentsService appointmentsService, InvoiceService invoiceService){
        if (appointmentsService == null) {
            throw new IllegalArgumentException("appointmentsService must not be null");
        }
        if (invoiceService == null) {
            throw new IllegalArgumentException("invoiceService must not be null");
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
