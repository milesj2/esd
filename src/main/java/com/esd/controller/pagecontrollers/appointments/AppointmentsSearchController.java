package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.service.AppointmentsService;

import org.joda.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments/schedule")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentsSearchController extends HttpServlet {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    private static final ArrayList<String> AppointmentKeys = new ArrayList<String>(Arrays.asList(
            DaoConsts.APPOINTMENT_SLOTS,
            DaoConsts.APPOINTMENT_STATUS,
            DaoConsts.ID));


    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) && !request.getParameter(key).isEmpty() && request.getParameter(key) != ""){
            return true;
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/appointments/scheduleAppointment.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

            Map<String, Object> args =  new HashMap<>();
            for(String key: AppointmentKeys) {
                if(checkRequestContains(request, key)){
                    args.put(key, request.getParameter(key));
                }
            }

            try {
                // default of today todo filter for user
                LocalDate fromDate = LocalDate.now();
                LocalDate toDate = LocalDate.now();

                if(checkRequestContains(request, "fromDate")) {
                    fromDate = LocalDate.parse(request.getParameter("fromDate"));
                }

                if(checkRequestContains(request, "toDate")) {
                    toDate = LocalDate.parse(request.getParameter("toDate"));
                }

                List<Appointment> appointmentList = appointmentsService.getAppointmentsInRange(fromDate, toDate, Optional.ofNullable(args));
                request.setAttribute("table", appointmentList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/appointments/scheduleAppointment.jsp");
            requestDispatcher.forward(request, response);
    }
}
