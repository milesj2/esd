package com.esd.controller.appointments;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.UserService;
import com.esd.views.LoginErrors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments")
public class AppointmentsController extends HttpServlet {

    private ArrayList<String> formValues =  new ArrayList<String>(Arrays.asList(
            DaoConsts.APPOINTMENT_ID,
            DaoConsts.APPOINTMENT_FROMDATE,
            DaoConsts.APPOINTMENT_TODATE,
            DaoConsts.APPOINTMENT_STATUS));

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
            response.sendRedirect("appointment/appointments.jsp"); //appointments page
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try {

            List<Appointment> appointmentList = AppointmentsService.getInstance().getAppointments(
                           formValues, request);

            request.setAttribute("table", appointmentList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("appointment/appointments.jsp");
            requestDispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
