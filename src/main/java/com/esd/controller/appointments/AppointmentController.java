package com.esd.controller.appointments;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.AppointmentStatus;
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
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

/**
 * Original Author: Trent meier
 * Use: the appointment controller provides appointment functionality for a user
 */

@WebServlet("/appointment")
public class AppointmentController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User) (request.getSession().getAttribute("currentSessionUser"));
        if (currentUser == null) {
            response.sendRedirect("../../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN) { //todo add user group validation
            response.sendRedirect("../../index.jsp");
            return;
        }

        try {

            int idVal = Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID));
            Appointment appointment = AppointmentsService.getInstance().getAppointmentById(idVal);
            request.setAttribute("appointment", appointment);

            RequestDispatcher view = request.getRequestDispatcher("/appointment/appointment.jsp");
            view.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User) (request.getSession().getAttribute("currentSessionUser"));
        if (currentUser == null) {
            response.sendRedirect("../../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN) { //todo add user group validation
            response.sendRedirect("../../index.jsp");
            return;
        }

        try {

            int choice = Integer.parseInt(request.getParameter("option"));
            int idVal = Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID));

            Appointment appointment = new Appointment();
            appointment.setId(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID)));
            appointment.setAppointmentDate(Date.from(Instant.parse(request.getParameter(DaoConsts.APPOINTMENT_DATE))));
            appointment.setAppointmentTime(Date.from(Instant.parse(request.getParameter(DaoConsts.APPOINTMENT_TIME))));
            appointment.setSlots(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
            appointment.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
            appointment.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
            appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(DaoConsts.APPOINTMENT_STATUS)));

            if(choice == 0) {
                //update user with request val
                AppointmentsService.getInstance().updateAppointmentById(appointment);

                //success and get updated appointment
                request.setAttribute("message", "Success");
                appointment = AppointmentsService.getInstance().getAppointmentById(idVal);
            } else {
                // create
                AppointmentsService.getInstance().createNewAppointment(appointment);

                //success and get updated appointment
                request.setAttribute("message", "Success");
                appointment = AppointmentsService.getInstance().getAppointmentById(idVal);
            }

            // dispatch
            RequestDispatcher view = request.getRequestDispatcher("/appointment/appointment.jsp");
            view.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}