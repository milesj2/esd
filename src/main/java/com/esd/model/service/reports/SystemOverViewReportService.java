package com.esd.model.service.reports;

import com.esd.model.dao.UserDao;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.service.UserService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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

    private SystemOverViewReportService() {
    }

    public synchronized static SystemOverViewReportService getInstance(){
        if(instance == null){
            instance = new SystemOverViewReportService();
        }
        return instance;
    }

    public HashMap<String, String> getReportData(){

        //TODO get the actual response data
        HashMap<String, String> responseData = new HashMap<>();

        responseData.put(APPOINTMENTSMADE, "0");
        responseData.put(APPOINTMENTSCANCELED, "0");
        responseData.put(APPOINTMENTSCANCELRATE, "1");
        responseData.put(INVOICESMADE, "2");
        responseData.put(INVOICESPAID, "3");
        responseData.put(INVOICESUNPAID, "4");
        responseData.put(INVOICESOVERDUE, "5");
        responseData.put(OVERALLINVOICESUNPAID, "6");
        responseData.put(OVERALLINVOICESOVERDUE, "7");
        responseData.put(TOTALINCOME, "8");
        responseData.put(OUTSTANDINGINCOME, "9");
        return responseData;
    }

    public int getAppointmentsCanceled(){
        return 0;
    }

    public int getAppointmentsMade(){
        return 0;
    }

    public double calculateCancelationRate(int made, int canceled){
        return canceled / made;
    }

    public int getInvoicesMade(){
    return 0;
    }


}
