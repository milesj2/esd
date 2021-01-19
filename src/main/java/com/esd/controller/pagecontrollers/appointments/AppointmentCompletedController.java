package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/appointments/completed")
@Authentication(userGroups = {UserGroup.DOCTOR, UserGroup.NURSE})
public class AppointmentCompletedController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if( request.getParameter("selectedAppointmentId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }

        int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
        Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);

        request.setAttribute("scheduleLink", UrlUtils.absoluteUrl(request, "/appointments/schedule"));
        request.setAttribute("invoiceDownloadLink", "");
        request.setAttribute("prescriptionViewLink", "");
        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentCompleted.jsp");
        view.forward(request, response);
    }
}
