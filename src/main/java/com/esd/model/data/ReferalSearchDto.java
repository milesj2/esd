package com.esd.model.data;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ReferalSearchDto {

    private int id;
    private String patientFirstName;
    private String patientLastName;
    private String practitionerFirstName;
    private String practitionerLastName;
    private String thirdPartyAdress1;
    private String thirdPartyName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getPractitionerFirstName() {
        return practitionerFirstName;
    }

    public void setPractitionerFirstName(String practitionerFirstName) {
        this.practitionerFirstName = practitionerFirstName;
    }

    public String getPractitionerLastName() {
        return practitionerLastName;
    }

    public void setPractitionerLastName(String practitionerLastName) {
        this.practitionerLastName = practitionerLastName;
    }

    public String getThirdPartyAdress1() {
        return thirdPartyAdress1;
    }

    public void setThirdPartyAdress1(String thirdPartyAdress1) {
        this.thirdPartyAdress1 = thirdPartyAdress1;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
