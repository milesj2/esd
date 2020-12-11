package com.esd.model.service;

import com.esd.model.dao.InvoiceDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, used to access invoice data objs
 */

public class InvoiceService {

    private static InvoiceService instance;
    private InvoiceDao invoiceDao;

    private InvoiceService(InvoiceDao invoiceDao) {
        if(invoiceDao == null){
            throw new IllegalArgumentException("invoiceDao must not be null");
        }
        this.invoiceDao = invoiceDao;
    }

    public synchronized static InvoiceService getInstance(){
        if(instance == null){
            instance = new InvoiceService(InvoiceDao.getInstance());
        }
        return instance;
    }

    public static ArrayList<Invoice> getInvoiceFromFilteredRequest(ArrayList<String> formKeys,
                                                                   HttpServletRequest request) {
        ArrayList<Invoice> invoices = InvoiceDao.getInstance().getFilteredDetails(formKeys, request);
        return invoices;
    }

}
