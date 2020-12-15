package com.esd.controller.search;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.User;
import com.esd.model.service.InvoiceService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the invoice search controller provides invoice access filtering and redirects to
 * user search page
 */

@WebServlet("/invoiceSearch")
public class InvoiceSearchController extends HttpServlet {

    private ArrayList<String> invoiceFormsConst = new ArrayList<String>(Arrays.asList(
            DaoConsts.ID,
            DaoConsts.INVOICE_DATE,
            DaoConsts.INVOICE_STATUS ,
            DaoConsts.INVOICE_TIME,
            DaoConsts.INVOICE_STATUS,
            DaoConsts.EMPLOYEE_ID_FK,
            DaoConsts.PATIENT_ID_FK,
            DaoConsts.APPOINTMENT_ID_FK));

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("../../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN){ //todo add user group validation
            response.sendRedirect("../../index.jsp");
            return;
        }

        try {
            response.sendRedirect("search/invoiceSearch.jsp"); //logged-in page
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }

    private static Map<String, Object> get(HttpServletRequest request, List<String> map){
        Map<String, Object> retVal = new HashMap<>();
        for(String s : map){
            Object value = request.getAttribute(s);
            if(value != null){
                retVal.put(s, value);
            }
        }
        return retVal;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try {
            ArrayList<Invoice> invoiceList = InvoiceService.getInvoiceFromFilteredRequest(invoiceFormsConst, request);

            request.setAttribute("table", invoiceList);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/invoiceSearch.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
}
