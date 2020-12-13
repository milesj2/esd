package com.esd.model.data.persisted;

import java.util.Date;

/**
 * Original Author: Angela Jackson
 * Use: This class is a simple data class used to store the data about an prescriptions
 */
public class Prescription {
    private int id;
    private int employeeId;
    private int patientId;
    private String prescriptionDetails;
    private int appointmentId;
    private Date issueDate;

    public Prescription (int id, int employeeId, int patientId, String prescriptionDetails, int appointmentId, Date issueDate)
    {
        this.id = id;
        this.employeeId = employeeId;
        this.patientId = patientId;
        this.prescriptionDetails = prescriptionDetails;
        this.appointmentId = appointmentId;
        this.issueDate = issueDate;
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
    
    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    
}
