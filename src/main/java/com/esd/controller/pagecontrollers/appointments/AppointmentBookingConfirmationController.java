package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.AppointmentPlaceHolder;
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
import java.io.IOException;

@WebServlet("/appointments/confirm")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentBookingConfirmationController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("pageTitle", "Bookings");

        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentBookingConfirmation.jsp");
        view.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("action").equals("book")){
            if (request.getParameter("selectedAppointmentId") != null){
                int id = Integer.parseInt(request.getParameter("selectedAppointmentId"));
                Appointment appointment = AppointmentsService.getInstance().getAppointmentById(id);
                request.setAttribute("originalAppointment", appointment);
            }
            request.setAttribute("confirmed", false);
            RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentBookingConfirmation.jsp");
            view.forward(request, response);
        }

        if(request.getParameter("action").equals("confirm")){
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));

            int patientId = Integer.parseInt(request.getParameter("employeeId"));

            int slots = Integer.parseInt(request.getParameter("slots"));
            LocalDate appointmentDate = new LocalDate(request.getParameter("appointmentDate"));
            LocalTime appointmentTime = new LocalTime(request.getParameter("appointmentTime"));

            AppointmentPlaceHolder appointmentPlaceHolder = new AppointmentPlaceHolder(employeeId, appointmentDate, appointmentTime, slots);

            boolean success;
            if (request.getParameter("selectedAppointmentId") != null){
                int id = Integer.parseInt(request.getParameter("selectedAppointmentId"));
                success = AppointmentsService.getInstance().updateAppointment(id, appointmentPlaceHolder, patientId);
            }else{
                success = AppointmentsService.getInstance().bookAppointment(appointmentPlaceHolder, patientId);
            }

            if(success){
                request.setAttribute("confirmed", true);
                request.setAttribute("confirmationMessage", "Appointment successfully booked");
            }else{
                request.setAttribute("confirmed", false);
                request.setAttribute("errorMessage", "Something went wrong confirming appointment");
            }

            RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentBookingConfirmation.jsp");
            view.forward(request, response);
        }

    }
}
