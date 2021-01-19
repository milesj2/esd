package com.esd.model.data.persisted;


import org.joda.time.LocalDate;

/**
 * Original Author: Angela Jackson
 * Use: This class is a simple data class used to store the data about an prescriptions
 */
public class Prescription {
    private int id;
    private Integer originatingPrescriptionId;
    private int employeeId;
    private int patientId;
    private String prescriptionDetails;
    private int appointmentId;
    private LocalDate issueDate;

    public Prescription() {
    }

    public Integer getOriginatingPrescriptionId() {
        return originatingPrescriptionId;
    }

    public void setOriginatingPrescriptionId(int originatingPrescriptionId) {
        this.originatingPrescriptionId = originatingPrescriptionId;
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

    public String getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(String prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }
    
     public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
    
}
