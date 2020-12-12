package com.esd.controller.search;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import javax.servlet.annotation.WebServlet;
import com.esd.model.service.PrescriptionService;
import com.esd.model.service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Angela Jackson
 * Use: the prescription create controller validates the user and then redirects to the new prescription 
 * page
 */

@WebServlet("/createPrescription")
public class PrescriptionCreateController extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String doctorFirstName = request.getParameter("doctorFirstName");
        String doctorLastName = request.getParameter("doctorLastName");
        String patientFirstName = request.getParameter("patientFirstName");
        String patientLastName = request.getParameter("patientLastName");
        String prescriptionDetails = request.getParameter("prescriptionDetails");
        String appointmentId = request.getParameter("appointmentId");
        String issueDate = request.getParameter("issueDate");

        String notify = "";
        try {
            boolean prescriptionCreated = PrescriptionService.getInstance()
                .createPrescription(doctorFirstName, doctorLastName, patientFirstName, patientLastName, prescriptionDetails, appointmentId, issueDate);
            if (prescriptionCreated) {
              notify = "Sucess! Prescription is recorded.";
            }
            else {
              notify = "Error: Either Doctor or Patient Name is Incorrect. Try Again";
            }
        }//endtry
        catch (Exception e) {
          System.out.println(e);
          response.sendRedirect("index.jsp?err=true"); //error page
        }
         
        request.setAttribute("notify",notify);
        RequestDispatcher view = request.getRequestDispatcher("/createPrescription.jsp");
        view.forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN){ //todo add user group validation
            response.sendRedirect("../index.jsp");
            return;
        }

        try {
            response.sendRedirect("createPrescription.jsp"); //logged-in page
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
   
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
