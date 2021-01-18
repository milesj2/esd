package com.esd.model.service;

import com.esd.model.dao.DaoConsts;
import com.esd.model.dao.InvoiceDao;
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

    public List<Invoice> getInvoiceFromFilteredRequest(Map<String, Object> args) {
        try {
            return invoiceDao.getInstance().getFilteredDetails(args);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createInvoice(Invoice invoice, List<InvoiceItem> invoiceItems) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().createInvoice(invoice, invoiceItems);
    }

    public void updateInvoice(Invoice invoice, List<InvoiceItem> invoiceItems) throws InvalidIdValueException, SQLException {
        invoiceDao.getInstance().updateInvoice(invoice, invoiceItems);
    }

    public InvoiceItem deriveInvoiceItemAttributes(Invoice invoice) throws InvalidIdValueException, SQLException {
        InvoiceItem invoiceItem = new InvoiceItem();
        SystemUser employeeUser = SystemUserDao.getInstance().getUserByID(invoice.getEmployeeId());
        Double invoiceCost = null;

        if(employeeUser.getUserGroup() == UserGroup.DOCTOR){
            invoiceCost = SystemSettingDao.getInstance().getDoubleSettingValueByKey("baseConsultationFeeDoctor");
        } else if(employeeUser.getUserGroup() == UserGroup.NURSE){
            invoiceCost = SystemSettingDao.getInstance().getDoubleSettingValueByKey("baseConsultationFeeNurse");
        } else {
            throw new InvalidIdValueException("appointment employee must be doctor or nurse");
        }

        if(invoice.getId()!=0){
            invoiceItem.setInvoiceId(invoice.getId());
        }
        invoiceItem.setCost(invoiceCost);

        return invoiceItem;
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

        //create Invoice Item List from appointment
        ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
        InvoiceItem invoiceItem = deriveInvoiceItemAttributes(invoiceDao.getInstance().getInvoiceByAppointmentId(appointment.getId()));

        //additional non-invoice derived attributes
        invoiceItem.setInvoiceId(invoice.getId());
        invoiceItem.setQuantity(appointment.getSlots());
        invoiceItem.setDescription("Invoice for: " + appointment.getAppointmentDate().toString());

        invoiceItems.add(invoiceItem);

        createInvoice(invoice, invoiceItems);
    }
}
