package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.AppointmentOptions;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.service.AppointmentsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Original Author: Trent meier
 * Use: the appointment controller provides appointment functionality for a user
 */

@WebServlet("/appointments/view")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentController extends HttpServlet {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private AppointmentsService appointmentsService = AppointmentsService.getInstance();


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

            try {
                // if referred with get url get appointments
                if(request.getParameterMap().containsKey(DaoConsts.ID)) {
                    int idVal = Integer.parseInt(request.getParameter(DaoConsts.ID));
                    Appointment appointment = appointmentsService.getAppointmentById(idVal);
                    request.setAttribute("appointment", appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointment.jsp");
            view.forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        try {
            int idVal = Integer.parseInt(request.getParameter(DaoConsts.ID));

            Appointment appointment = new Appointment();
            appointment.setId(Integer.parseInt(request.getParameter(DaoConsts.ID)));
            appointment.setAppointmentDate(dateFormat.parse(request.getParameter(DaoConsts.APPOINTMENT_DATE)));
            appointment.setAppointmentTime(dateFormat.parse("14:15:00")); //todo figure out time format
            appointment.setSlots(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
            appointment.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
            appointment.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
            appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(DaoConsts.APPOINTMENT_STATUS)));

            if(request.getParameter("option") == AppointmentOptions.UPDATE.toString()) {
                appointmentsService.updateAppointment(appointment);
            } else {
                appointmentsService.createNewAppointment(appointment);
            }

            request.setAttribute("message", "Success");
            appointment = appointmentsService.getAppointmentById(idVal);
            request.setAttribute("appointment", appointment);

        } catch (Exception e){
            e.printStackTrace();
        }

        // dispatch
        RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointment.jsp");
        view.forward(request, response);
    }
}