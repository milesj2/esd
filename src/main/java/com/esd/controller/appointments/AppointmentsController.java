package com.esd.controller.appointments;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.User;
import com.esd.model.service.AppointmentsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments")
public class AppointmentsController extends HttpServlet {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final ArrayList<String> AppointmentKeys = new ArrayList<String>(Arrays.asList(
            DaoConsts.APPOINTMENT_SLOTS,
            DaoConsts.APPOINTMENT_STATUS,
            DaoConsts.ID));

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

    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) && !request.getParameter(key).isEmpty() && request.getParameter(key) != ""){
            return true;
        }
        return false;
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
            Map<String, Object> args =  new HashMap<>();
            for(String key: AppointmentKeys) {
                if(checkRequestContains(request, key)){
                    args.put(key, request.getParameter(key));
                }
            }

            try {
                // default of today todo get today
                Date fromDate = dateFormat.parse("2020-11-01");
                Date toDate = dateFormat.parse("2020-12-31");

                if(checkRequestContains(request, "fromDate")) {
                    fromDate = dateFormat.parse(request.getParameter("fromDate"));
                }

                if(checkRequestContains(request, "toDate")) {
                    toDate = dateFormat.parse(request.getParameter("toDate"));
                }

                List<Appointment> appointmentList = appointmentsService.getAppointmentsInRange(fromDate, toDate, Optional.ofNullable(args));
                request.setAttribute("table", appointmentList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("appointment/appointments.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
