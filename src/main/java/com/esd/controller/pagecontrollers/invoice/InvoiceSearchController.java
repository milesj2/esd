package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
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
@WebServlet("/invoices/search")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoiceSearchController extends HttpServlet {

    private InvoiceService invoiceService = InvoiceService.getInstance();
    private ArrayList<String> invoiceFormsConst = new ArrayList<String>(Arrays.asList(
            DaoConsts.ID,
            DaoConsts.INVOICE_DATE,
            DaoConsts.INVOICE_STATUS,
            DaoConsts.INVOICE_TIME,
            DaoConsts.INVOICE_STATUS,
            DaoConsts.EMPLOYEE_ID,
            DaoConsts.PATIENT_ID,
            DaoConsts.ID));

    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) &&
                !request.getParameter(key).isEmpty() &&
                request.getParameter(key) != ""){
            return true;
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        RequestDispatcher view = request.getRequestDispatcher("/invoices/searchInvoices.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        Map<String, Object> args =  new HashMap<>();
        for(String key: invoiceFormsConst) {
            if(checkRequestContains(request, key)){
                args.put(key, request.getParameter(key));
            }
        }

        try {
            List<Invoice> invoiceList = invoiceService.getInvoiceFromFilteredRequest(args);

            request.setAttribute("table", invoiceList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/invoices/searchInvoices.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
