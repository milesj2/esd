package com.esd.model.service;

import com.esd.model.dao.DaoConsts;
import com.esd.model.dao.InvoiceDao;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.dao.SystemSettingDao;
import com.esd.model.dao.SystemUserDao;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.data.persisted.SystemUser;

import com.esd.model.exceptions.InvalidIdValueException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     
    public List<InvoiceItem> getAllInvoiceItemsForInvoiceId(int id){
       try {
           return invoiceDao.getAllInvoiceItemsForInvoiceId(id);
       } catch (SQLException ex) {
           Logger.getLogger(InvoiceService.class.getName()).log(Level.SEVERE, null, ex);
       }
       return new ArrayList<>();
    }
    
    public InvoiceItem getInvoiceItemById(int id) throws SQLException, InvalidIdValueException {
        return invoiceDao.getInvoiceItemById(id);
    }

    public List<Invoice> getInvoiceFromFilteredRequest(Map<String, Object> args) {
        try {
            return invoiceDao.getInstance().getFilteredDetails(args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createInvoice(Invoice invoice) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().createInvoice(invoice);
    }
    
    public void updateInvoiceStatus(Integer id, String invoiceStatus) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().updateInvoiceStatus(id, invoiceStatus);
    }

    public void createInvoiceItem(InvoiceItem invoiceItem) throws SQLException {
        invoiceDao.getInstance().createInvoiceItem(invoiceItem);
    }

    public void updateInvoice(Invoice invoice) {
        try {
            invoiceDao.getInstance().updateInvoice(invoice);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InvalidIdValueException e) {
            e.printStackTrace();
        }
    }

    public void createInvoiceFromAppointment(Appointment appointment) throws InvalidIdValueException, SQLException {

        //create invoice
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(appointment.getAppointmentDate());
        invoice.setInvoiceTime(appointment.getAppointmentTime());
        invoice.setInvoiceStatus(InvoiceStatus.UNPAID);
        invoice.setInvoiceStatusChangeDate(appointment.getAppointmentDate());
        invoice.setEmployeeId(appointment.getEmployeeId());
        invoice.setPatientId(appointment.getPatientId());
        SystemUser systemUser = SystemUserService.getInstance().getUserByID(appointment.getPatientId());
        invoice.setPrivatePatient(systemUser.getUserGroup().equals(DaoConsts.PRIVATE_PATIENT));
        invoice.setAppointmentId(appointment.getId());

        createInvoice(invoice);

        //create Invoice Item
        invoice = invoiceDao.getInstance().getInvoiceByAppointmentId(appointment.getId());
        SystemUser employeeUser = SystemUserDao.getInstance().getUserByID(appointment.getEmployeeId());

        Double invoiceCost = null;
        if(employeeUser.getUserGroup() == UserGroup.DOCTOR){
            invoiceCost = SystemSettingDao.getInstance().getDoubleSettingValueByKey("baseConsultationFeeDoctor");
        } else if(employeeUser.getUserGroup() == UserGroup.NURSE){
            invoiceCost = SystemSettingDao.getInstance().getDoubleSettingValueByKey("baseConsultationFeeNurse");
        } else {
            throw new InvalidIdValueException("appointment employee must be doctor or nurse");
        }

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getId());
        invoiceItem.setCost(invoiceCost);
        invoiceItem.setQuantity(appointment.getSlots());
        invoiceItem.setDescription("Invoice for: " + appointment.getAppointmentDate().toString());

        createInvoiceItem(invoiceItem);
    }
}
