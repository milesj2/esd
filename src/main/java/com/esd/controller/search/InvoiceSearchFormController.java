package com.esd.controller.search;

import com.esd.model.dao.ConnectionManager;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Original Author: Trent meier
 * Use: the invoice search form controller returns matching results from the search form's
 * parameters and redirects to the invoice search page
 */

@WebServlet("/invoiceSearchForm")
public class InvoiceSearchFormController extends HttpServlet {
    private ArrayList<String> invoiceFormsConst = new ArrayList<String>(Arrays.asList(
            DaoConsts.INVOICE_ID,
            DaoConsts.INVOICE_DATE,
            DaoConsts.INVOICE_STATUS ,
            DaoConsts.INVOICE_TIME,
            DaoConsts.INVOICE_STATUS,
            DaoConsts.EMPLOYEE_ID,
            DaoConsts.PATIENT_ID,
            DaoConsts.APPOINTMENT_ID));

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        ArrayList<Invoice> invoiceItemArrayList = new ArrayList<Invoice>();
        boolean where = true;
        String STATEMENT_BUILDER = "SELECT * FROM ADMIN.INVOICE ";

        try { //todo break out into seprate service
            // build query from form data
            for(String Value: invoiceFormsConst){
                if(!request.getParameter(Value).isEmpty()) {
                    if(where){
                        STATEMENT_BUILDER += "WHERE " + Value + " = ?";
                        where = false;
                    } else {
                        STATEMENT_BUILDER += " AND " + Value + " = ?";
                    }
                }
            }

            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(STATEMENT_BUILDER);

            //set statement values
            int i=1;
            for(String Value: invoiceFormsConst) {
                if (!request.getParameter(Value).isEmpty()) {
                    String val = request.getParameter(Value);
                    statement.setString(i, val);
                    i += 1;
                }
            }

            ResultSet result = statement.executeQuery();

            // add results to list of user to return
            while(result.next()){
                Invoice invoice =  new Invoice();
                invoice.setInvoiceDate(result.getDate(DaoConsts.INVOICE_DATE));
                invoice.setInvoiceTime(result.getTime(DaoConsts.INVOICE_TIME));
                invoice.setInvoiceStatus(InvoiceStatus.valueOf(InvoiceStatus.class,
                        result.getString(DaoConsts.INVOICE_STATUS).toUpperCase()));
                invoice.setEmployeeId(result.getInt(DaoConsts.EMPLOYEE_ID));
                invoice.setPatientId(result.getInt(DaoConsts.PATIENT_ID));
                invoice.setPrivatePatient(result.getBoolean(DaoConsts.PRIVATE_PATIENT));
                invoice.setAppointmentId(result.getInt(DaoConsts.APPOINTMENT_ID));

                invoiceItemArrayList.add(invoice);
            }

            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("table", invoiceItemArrayList);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/invoiceSearch.jsp");
        requestDispatcher.forward(request, response);
    }
}
