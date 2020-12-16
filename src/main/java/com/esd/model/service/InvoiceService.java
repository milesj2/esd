package com.esd.model.service;

import com.esd.model.dao.InvoiceDao;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

    public Invoice getInvoiceById(int id) throws SQLException, InvalidIdValueException {
        return invoiceDao.getInvoiceById(id);
    }

    public List<Invoice> getInvoiceFromFilteredRequest(Map<String, Object> args) throws SQLException {
        return invoiceDao.getInstance().getFilteredDetails(args);
    }

    public void createInvoice(Invoice invoice) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().createInvoice(invoice);
    }

    public void updateInvoice(Invoice invoice) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().updateInvoice(invoice);
    }
}
