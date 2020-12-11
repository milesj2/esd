package com.esd.model.dao;

import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;

import javax.servlet.http.HttpServletRequest;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to invoices
 */
public class InvoiceDao {

    private static InvoiceDao instance;

    private static final String GET_FILTERED_INVOICES = "SELECT * FROM INVOICE";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String MATCH = " = ?";

    private static String LOADCOMPLETEINVOICEDATERESTRICTIONWITHSTATUS = "select * from invoice\n" +
            "    left outer join INVOICEITEM on INVOICEITEM.invoiceID = invoice.id\n" +
            "    where INVOICEDATE > ? AND INVOICEDATE < ? AND STATUS = ?";

    private InvoiceDao() {

    }

    public Invoice extractInvoiceFromResultSet(ResultSet resultSet){
        Invoice invoice =  new Invoice();
        invoice.setId(resultSet.getInt(DaoConsts.INVOICE_ID));
        invoice.setInvoiceDate(resultSet.getDate(DaoConsts.INVOICE_DATE));
        invoice.setInvoiceTime(resultSet.getTime(DaoConsts.INVOICE_TIME));
        invoice.setInvoiceStatus(InvoiceStatus.valueOf(InvoiceStatus.class,
                resultSet.getString(DaoConsts.INVOICE_STATUS).toUpperCase()));
        invoice.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID));
        invoice.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID));
        invoice.setPrivatePatient(resultSet.getBoolean(DaoConsts.PRIVATE_PATIENT));
        invoice.setAppointmentId(resultSet.getInt(DaoConsts.APPOINTMENT_ID));
        return null;
    }

    public void getAllInvoicesAndItemsBetweenDatesAndWithStatus(Date start, Date end, InvoiceStatus status) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(LOADCOMPLETEINVOICEDATERESTRICTIONWITHSTATUS);
        statement.setDate(1, new java.sql.Date(start.getTime()));
        statement.setDate(2, new java.sql.Date(start.getTime()));
        statement.setString(3, status.name());

        ResultSet result = statement.executeQuery();
        boolean resultFound;

        while ((resultFound = result.next())){

        }
    }

    public void getAllInvoiceItemsForInvoiceId(long id){

    }

    public synchronized static InvoiceDao getInstance(){
        if(instance == null){
            instance = new InvoiceDao();
        }
        return instance;
    }

    public static ArrayList<Invoice> getFilteredDetails(ArrayList<String> formKey, HttpServletRequest request){

        ArrayList<Invoice> invoiceLst = new ArrayList<Invoice>();
        String STATEMENT_BUILDER = GET_FILTERED_INVOICES;

        try {
            boolean first = true;
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    if(first){
                        STATEMENT_BUILDER += WHERE+key+MATCH;
                        first = false;
                    } else {
                        STATEMENT_BUILDER += AND+key+MATCH;
                    }
                }
            }

            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(STATEMENT_BUILDER);

            int i=1;  //set statement values
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    statement.setString(i,(String)request.getParameter(key));
                    i+=1;
                }
            }

            ResultSet result = statement.executeQuery();

            // add results to list of user to return
            while(result.next()){
                Invoice invoice =  new Invoice();
                invoice.setId(result.getInt(DaoConsts.INVOICE_ID));
                invoice.setInvoiceDate(result.getDate(DaoConsts.INVOICE_DATE));
                invoice.setInvoiceTime(result.getTime(DaoConsts.INVOICE_TIME));
                invoice.setInvoiceStatus(InvoiceStatus.valueOf(InvoiceStatus.class,
                        result.getString(DaoConsts.INVOICE_STATUS).toUpperCase()));
                invoice.setEmployeeId(result.getInt(DaoConsts.EMPLOYEE_ID));
                invoice.setPatientId(result.getInt(DaoConsts.PATIENT_ID));
                invoice.setPrivatePatient(result.getBoolean(DaoConsts.PRIVATE_PATIENT));
                invoice.setAppointmentId(result.getInt(DaoConsts.APPOINTMENT_ID));

                invoiceLst.add(invoice);
            }

            // close statement and result set
            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoiceLst;
    }
}

