package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.service.AppointmentsService;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Original Author: Trent meier
 * Use: the appointment controller provides appointment functionality for a user
 */

@WebServlet("/appointments/viewAppointment")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentController extends HttpServlet {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        if (!request.getParameterMap().containsKey("selectedAppointmentId")) {
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }

        request.setAttribute("pageTitle", "Appointments");
        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        int appId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
        Appointment appointment = appointmentsService.getAppointmentById(appId);
        request.setAttribute("appointment", appointment);

        RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointment.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        try {
            Appointment appointment = createAppointmentFromParameters(request);
            int appId = appointment.getId();

            if (request.getParameter("inprogress") != null) { // if wanting to set appointment as 'in progress'
                response.sendRedirect(UrlUtils.absoluteUrl(request,"appointments/inprogress?selectedAppointmentId=" + appId));
                return;
            } else if (request.getParameter("amend") != null) { // if wanting to amend an appointment
                response.sendRedirect(UrlUtils.absoluteUrl(request, "appointments/book?selectedAppointmentId=" + appId));
                return;
            } else if (request.getParameter("cancel") != null) { // cancel appointment
                appointment.setStatus(AppointmentStatus.CANCELED);
                appointmentsService.updateAppointment(appointment);
                request.setAttribute("msg", "Successfully canceled.");
                request.setAttribute("appointment", appointment);
            }
        } catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // dispatch
        RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointment.jsp");
        view.forward(request, response);
    }

    private Appointment createAppointmentFromParameters(HttpServletRequest request) {
        Appointment appointment = new Appointment();
        appointment.setId(Integer.parseInt(request.getParameter(DaoConsts.ID)));
        appointment.setAppointmentDate(LocalDate.parse(request.getParameter(DaoConsts.APPOINTMENT_DATE)));
        appointment.setAppointmentTime(LocalTime.parse(request.getParameter(DaoConsts.APPOINTMENT_TIME)));
        appointment.setSlots(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
        appointment.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
        appointment.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
        appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(DaoConsts.APPOINTMENT_STATUS)));
        return appointment;
    }
}
