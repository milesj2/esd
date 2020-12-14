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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments")
public class AppointmentsController extends HttpServlet {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final ArrayList<String> AppointmentKeys = new ArrayList<String>(Arrays.asList(
            DaoConsts.ID,
            DaoConsts.APPOINTMENT_DATE,
            DaoConsts.APPOINTMENT_SLOTS,
            DaoConsts.EMPLOYEE_ID,
            DaoConsts.PATIENT_ID));

    private boolean getAuthorisationState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Validate user is logged in
        User currentUser = (User) (request.getSession().getAttribute("currentSessionUser"));
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN) { //todo add user group validation
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        if(getAuthorisationState(request,response)){
            response.sendRedirect("appointment/appointments.jsp"); //appointments page
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        if(getAuthorisationState(request, response)){
            Map<String, String> args = null;
            for(String key: AppointmentKeys) {
                if(!request.getParameter(key).isEmpty()){
                    args.put(key, request.getParameter(key));
                }
            }

            try {
                List<Appointment> appointmentList = AppointmentsService.getInstance().getAppointmentsInRange(
                        dateFormat.parse(request.getParameter("fromDate")),
                        dateFormat.parse(request.getParameter("toDate")),
                        args);
                request.setAttribute("table", appointmentList);
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("appointment/appointments.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
