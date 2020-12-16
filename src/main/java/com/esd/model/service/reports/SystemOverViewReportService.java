package com.esd.model.service.reports;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.dao.DaoConsts;
import com.esd.model.dao.InvoiceDao;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;

import java.sql.SQLException;
import java.util.*;

public class SystemOverViewReportService {
    private static SystemOverViewReportService instance;


    public static final String APPOINTMENTSMADE = "APPOINTMENTSMADE";
    public static final String APPOINTMENTSCANCELED = "APPOINTMENTSCANCELED";
    public static final String APPOINTMENTSCANCELRATE = "APPOINTMENTSCANCELRATE";
    public static final String INVOICESMADE = "INVOICESMADE";
    public static final String INVOICESPAID = "INVOICESPAID";
    public static final String INVOICESUNPAID = "INVOICESUNPAID";
    public static final String INVOICESOVERDUE = "INVOICESOVERDUE";
    public static final String TOTALINCOME = "TOTALINCOME";
    public static final String OUTSTANDINGINCOME = "OUTSTANDINGINCOME";
    public static final String OVERALLINVOICESUNPAID = "OVERALLINVOICESUNPAID";
    public static final String OVERALLINVOICESOVERDUE = "OVERALLINVOICESOVERDUE";
    public static final String PAIDTHISPERIOD = "PAIDTHISPERIOD";

    private InvoiceDao invoiceDao = InvoiceDao.getInstance();
    private AppointmentDao appointmentDao = AppointmentDao.getInstance();

    private SystemOverViewReportService() {
    }

    public synchronized static SystemOverViewReportService getInstance(){
        if(instance == null){
            instance = new SystemOverViewReportService();
        }
        return instance;
    }

    public HashMap<String, String> getReportData(Date start, Date end) throws SQLException {
        int invoicesCreated = invoiceDao.getAllInvoicesWithStatus(start, end, Optional.empty(), true).size();
        int invoicesUnpaid = invoiceDao.getAllInvoicesWithStatus(start, end, Optional.of(InvoiceStatus.UNPAID), true).size();
        int invoicesOverDue = invoiceDao.getAllInvoicesWithStatus(start, end, Optional.of(InvoiceStatus.OVERDUE), true).size();

        List<Invoice> invoicesPaid = invoiceDao.getAllInvoicesWithStatus(start, end, Optional.of(InvoiceStatus.PAID), true);

        List<Invoice> overallInvoicesUnpaid = invoiceDao.getAllInvoicesWithStatus(InvoiceStatus.UNPAID, true);
        List<Invoice> overallInvoicesOverDue = invoiceDao.getAllInvoicesWithStatus(InvoiceStatus.OVERDUE, true);

        //this is all the invoices paid this period, this can include elements from previous periods
        List<Invoice> paidThisPeriod = invoiceDao.getInvoiceWithStatusChangeToThisPeriod(start, end, InvoiceStatus.PAID, true);

        //Total the values from the streams, so we can work out the outstanding and total income.
        double totalIncome = paidThisPeriod.stream()
                .map(Invoice::getItems)
                .flatMap(List::stream)
                .mapToDouble(InvoiceItem::getCost)
                .sum();

        List<Invoice> toTotal = new ArrayList<>(overallInvoicesOverDue);
        toTotal.addAll(overallInvoicesUnpaid);

        double totalOutstanding = toTotal.stream()
                .map(Invoice::getItems)
                .flatMap(List::stream)
                .mapToDouble(InvoiceItem::getCost)
                .sum();

        int madeAppointments = appointmentDao.getAppointmentsInPeriodWithStatus(start, end,
                Optional.empty()).size();
        int canceledAppointments = appointmentDao.getAppointmentsInPeriodWithStatus(start, end,
                Optional.of(AppointmentStatus.CANCELED)).size();

        //Java likes casting for some reason
        int cancelRate = (int)(((double)canceledAppointments/(double)madeAppointments )* 100);

        HashMap<String, String> reportData = new HashMap<>();
        reportData.put(APPOINTMENTSMADE, String.valueOf(madeAppointments));
        reportData.put(APPOINTMENTSCANCELED, String.valueOf(canceledAppointments));
        reportData.put(APPOINTMENTSCANCELRATE, cancelRate + "%");
        reportData.put(INVOICESMADE, String.valueOf(invoicesCreated));
        reportData.put(INVOICESPAID, String.valueOf(invoicesPaid.size()));
        reportData.put(INVOICESUNPAID, String.valueOf(invoicesUnpaid));
        reportData.put(INVOICESOVERDUE, String.valueOf(invoicesOverDue));
        reportData.put(OVERALLINVOICESUNPAID, String.valueOf(overallInvoicesUnpaid.size()));
        reportData.put(OVERALLINVOICESOVERDUE, String.valueOf(overallInvoicesOverDue.size()));
        reportData.put(PAIDTHISPERIOD, String.valueOf(paidThisPeriod.size() - invoicesPaid.size()));
        reportData.put(TOTALINCOME, String.valueOf(totalIncome));
        reportData.put(OUTSTANDINGINCOME, String.valueOf(totalOutstanding));
        return reportData;
    }

    public int getAppointmentsCanceled(){
        return 0;
    }

    public int getAppointmentsMade(){
        return 0;
    }

    public int getInvoicesMade(){
    return 0;
    }
}
