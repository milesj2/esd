package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.AppointmentPlaceHolder;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.SystemUserService;
import com.esd.model.service.UserDetailsService;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/appointments/inprogress")
@Authentication(userGroups = {UserGroup.DOCTOR, UserGroup.NURSE})
public class AppointmentInProgressController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserGroup usergroup = AuthenticationUtils.getCurrentUserGroup(request);

        //We need a patient ID to start the booking process.
        if( request.getParameter("selectedAppointmentId") == null){
            return;
        }

        int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
        Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);

        UserDetails patientDetails = UserDetailsService.getInstance().getUserDetailsByID(appointment.getPatientId());
        UserDetails employeeDetails = UserDetailsService.getInstance().getUserDetailsByID(appointment.getPatientId());


        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentInProgress.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentInProgress.jsp");
        view.forward(request, response);
    }

    private class formData{
        private String appointmentNotes;

    }
}
