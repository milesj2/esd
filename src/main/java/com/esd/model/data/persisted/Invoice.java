package com.esd.model.data.persisted;

import com.esd.model.data.InvoiceStatus;

import java.util.Date;
import java.util.List;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about an invoice
 */
public class Invoice {

    private int id;
    private int employeeId;
    private int patientId;
    private int appointmentId;

    private boolean privatePatient;
    private Date invoiceDate;
    private Date invoiceTime; //Could use the Joda time libary which is a little nicer....
    private List<InvoiceItem> items;
    private InvoiceStatus invoiceStatus;

    public Invoice() {
    }

    public void AddInvoiceItem(InvoiceItem invoiceItem){
        this.items.add(invoiceItem);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public boolean isPrivatePatient() {
        return privatePatient;
    }

    public void setPrivatePatient(boolean privatePatient) {
        this.privatePatient = privatePatient;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(Date invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
