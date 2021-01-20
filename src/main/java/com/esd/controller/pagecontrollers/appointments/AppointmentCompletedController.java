package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.Prescription;
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

        request.setAttribute("pageTitle", "Appointments");

        if( request.getParameter("selectedAppointmentId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }

        try {
            int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
            Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);
            Invoice invoice = InvoiceService.getInstance().getInvoiceByAppointmentID(appointment.getId());
            Prescription prescription = PrescriptionService.getInstance().getPrescriptionForAppointment(appointment.getId());
            request.setAttribute("scheduleLink", UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            request.setAttribute("invoiceDownloadLink",
                    UrlUtils.absoluteUrl(request, "/invoices/pdf?selectedInvoiceID?="+invoice.getId()));
            request.setAttribute("prescriptionViewLink",
                    UrlUtils.absoluteUrl(request, "/prescriptions/view?selectedPrescriptionId="+prescription.getId()));
            RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentCompleted.jsp");
            view.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
