package com.esd.model.data.persisted;

import com.esd.model.data.InvoiceStatus;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

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
    private LocalDate invoiceStatusChangeDate;
    private boolean privatePatient;
    private LocalDate invoiceDate;
    private LocalTime invoiceTime;
    private List<InvoiceItem> items;
    private InvoiceStatus invoiceStatus;

    public Invoice() {
    }

    public void addInvoiceItem(InvoiceItem invoiceItem){
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

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalTime getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(LocalTime invoiceTime) {
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

    public LocalDate getInvoiceStatusChangeDate() {
        return invoiceStatusChangeDate;
    }

    public void setInvoiceStatusChangeDate(LocalDate invoiceStatusChangeDate) {
        this.invoiceStatusChangeDate = invoiceStatusChangeDate;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
        this.invoiceStatusChangeDate = new LocalDate();
    }
}
