package com.esd.model.dao;

/**
 * Original Author: Miles Jarvis
 * Use: This class is used for coding ease. It stores common strings associated with dao actions.
 */
public class DaoConsts {

    public final static String ID = "id";
    public final static String INVOICE_ID_FK = "invoiceId";
    public final static String PATIENT_ID_FK = "patientid";
    public final static String APPOINTMENT_ID_FK = "appointmentid";
    public final static String EMPLOYEE_ID_FK = "employeeid";
    public final static String SYSTEMUSER_ID_FK = "userId";
    public final static String WORKINGHOURS_ID_FK = "workingHoursId";
    public final static String THIRDPARTY_ID_FK = "thirdPartyId";

    public static final String TABLE_THIRDPARTY = "thirdParty";
    public static final String TABLE_THIRDPARTY_REFERENCE = TABLE_THIRDPARTY + ".";

    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_APPOINTMENTS_REFERENCE = TABLE_APPOINTMENTS + ".";

    public static final String TABLE_INVOICE = "invoice";
    public static final String TABLE_INVOICE_REFERENCE = TABLE_INVOICE + ".";

    public static final String TABLE_INVOICEITEM = "invoiceItem";
    public static final String TABLE_INVOICEITEM_REFERENCE = TABLE_INVOICEITEM + ".";

    public static final String TABLE_PRESCRIPTIONS = "prescriptions";
    public static final String TABLE_PRESCRIPTIONS_REFERENCE = TABLE_PRESCRIPTIONS + ".";

    public static final String TABLE_SYSTEMSETTING = "systemSetting";
    public static final String TABLE_SYSTEMSETTING_REFERENCE = TABLE_SYSTEMSETTING + ".";

    public static final String TABLE_SYSTEMUSER = "systemUser";
    public static final String TABLE_SYSTEMUSER_REFERENCE = TABLE_SYSTEMUSER + ".";

    public static final String TABLE_USERDETAILS = "userDetails";
    public static final String TABLE_USERDETAILS_REFERENCE = TABLE_USERDETAILS + ".";

    public static final String TABLE_WORKINGHOURS = "workingHours";
    public static final String TABLE_WORKINGHOURS_REFERENCE = TABLE_WORKINGHOURS + ".";

    public static final String TABLE_WORKINGHOURS_JT = "workingHoursJT";
    public static final String TABLE_WORKINGHOURS_JT_REFERENCE = TABLE_WORKINGHOURS_JT + ".";

    //foreign_keys
    public final static String INVOICE_ID = "invoiceId";
    public final static String PATIENT_ID = "patientid";
    public final static String EMPLOYEE_ID = "employeeid";


    public static final String WORKINGHOURS_STARTTIME = "startTime";
    public static final String WORKINGHOURS_ENDTIME = "endTime";
    public static final String WORKINGHOURS_WORKINGDAYS = "workingDays";

    public final static String SYSTEMUSER_ID = "Id";
    public final static String SYSTEMUSER_USERNAME = "username";
    public final static String SYSTEMUSER_PASSWORD = "password";
    public final static String SYSTEMUSER_USERGROUP = "usergroup";
    public final static String SYSTEMUSER_ACTIVE = "active";

    public final static String USERDETAILS_FIRSTNAME = "firstname";
    public final static String USERDETAILS_LASTNAME = "lastname";
    public final static String USERDETAILS_ADDRESS1 = "addressline1";
    public final static String USERDETAILS_ADDRESS2 = "addressline2";
    public final static String USERDETAILS_ADDRESS3 = "addressline3";
    public final static String USERDETAILS_TOWN = "town";
    public final static String USERDETAILS_POSTCODE = "postcode";
    public final static String USERDETAILS_DOB = "dob";

    public static final String INVOICE_STATUS_CHANGE_DATE = "statusChangeDate";

    public final static String INVOICE_DATE = "invoicedate";
    public final static String INVOICE_TIME = "invoicetime";
    public final static String INVOICE_STATUS = "invoicestatus";

    public final static String INVOICEITEM_COST = "itemcost";
    public final static String INVOICEITEM_QUANTITY = "quantity";
    public final static String INVOICEITEM_DESCRIPTION = "description";

    public final static String PRESCRIPTION_ID = "prescriptionid";
    public final static String PRESCRIPTION_DETAILS = "prescriptiondetails";
    public final static String PRESCRIPTION_ISSUE_DATE = "issuedate";
    public final static String PRESCRIPTION_ORIGINATING_PRESCRIPTION_ID = "originatingPrescriptionId";

    public final static String PRIVATE_PATIENT = "privatepatient";

    public static final String APPOINTMENT_DATE = "appointmentdate";
    public static final String APPOINTMENT_TIME = "appointmenttime";
    public static final String APPOINTMENT_SLOTS = "slots";
    public static final String APPOINTMENT_NOTES = "notes";
    public static final String APPOINTMENT_STATUS = "appointmentStatus";
    public static final String APPOINTMENT_APPOINTMENT_REASON = "appointmentReason";

    public final static String SYSTEMSETTING_SETTINGKEY = "settingKey";
    public final static String SYSTEMSETTING_SETTINGVALUE = "settingVal";
    
    public final static String THIRDPARTY_ID = "thirdPartyId";
    public final static String THIRDPARTY_NAME = "thirdPartyName";
    public final static String THIRDPARTY_ADDRESS1 = "addressline1";
    public final static String THIRDPARTY_ADDRESS2 = "addressline2";
    public final static String THIRDPARTY_ADDRESS3 = "addressline3";
    public final static String THIRDPARTY_TOWN = "town";
    public final static String THIRDPARTY_POSTCODE = "postCode";
    public final static String THIRDPARTY_TYPE = "thirdPartyType";
    public final static String THIRDPARTY_ACTIVE = "active";
}
