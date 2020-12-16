package com.esd.controller.invoice;

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

    private InvoiceService invoiceService = InvoiceService.getInstance();
    private ArrayList<String> invoiceFormsConst = new ArrayList<String>(Arrays.asList(
            DaoConsts.ID,
            DaoConsts.INVOICE_DATE,
            DaoConsts.INVOICE_STATUS ,
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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("../../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN){
            response.sendRedirect("../../index.jsp");
            return;
        }

        try {
            response.sendRedirect("search/invoiceSearch.jsp");
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
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
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/invoiceSearch.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
}
