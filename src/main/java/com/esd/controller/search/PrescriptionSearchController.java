package com.esd.controller.search;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.User;
import com.esd.model.service.PrescriptionService;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Original Author: Angela Jackson
 * Use: the prescription search controller validates the user and then redirects to the prescription search
 * page
 */

@WebServlet("/prescriptionSearch")
public class PrescriptionSearchController extends HttpServlet {

    //corresponds to user search form
    private ArrayList<String> formValues =  new ArrayList<String>(Arrays.asList(
        DaoConsts.PRESCRIPTION_ID,
        DaoConsts.EMPLOYEE_ID,
        DaoConsts.PATIENT_ID,
        DaoConsts.PRESCRIPTION_DETAILS,
        DaoConsts.APPOINTMENT_ID,
        DaoConsts.PRESCRIPTION_ISSUE_DATE));
    
    // serves page initially
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
            response.sendRedirect("search/prescriptionSearch.jsp"); //logged-in page
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
    
    //return search from data
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try {
            // pass request with form keys and request (has post values)
            ArrayList<Prescription> prescriptionList = PrescriptionService.getInstance().getPrescriptionFromFilteredRequest(formValues, request);
            
            //return prescription details list
            request.setAttribute("table", prescriptionList);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/prescriptionSearch.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
