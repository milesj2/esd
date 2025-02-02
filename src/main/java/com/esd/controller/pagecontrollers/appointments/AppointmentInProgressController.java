package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/appointments/inprogress")
@Authentication(userGroups = {UserGroup.DOCTOR, UserGroup.NURSE})
public class AppointmentInProgressController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserGroup usergroup = AuthenticationUtils.getCurrentUserGroup(request);

        request.setAttribute("pageTitle", "Appointments");

        //We need a patient ID to start the booking process.
        if( request.getParameter("selectedAppointmentId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }

        HttpSession session = request.getSession(false);
       FormData formData = (FormData) session.getAttribute("persistentFormData");
        session.removeAttribute("persistentFormData");
        if(formData != null){
           request.setAttribute("notes", formData.getAppointmentNotes());
           request.setAttribute("referalId", formData.getSelectedReferal());
        }else{
            request.setAttribute("notes", "");
            request.setAttribute("referalId", -1);
        }

        int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
        Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);

        if(appointment.getStatus() == AppointmentStatus.COMPLETE){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }

        if(appointment.getStatus() != AppointmentStatus.INPROGRESS){
            appointment.setStatus(AppointmentStatus.INPROGRESS);
            AppointmentsService.getInstance().updateAppointment(appointment);
        }

        Prescription prescription = PrescriptionService.getInstance().getPrescriptionForAppointment(appointment.getId());

        UserDetails patientDetails = UserDetailsService.getInstance().getUserDetailsByID(appointment.getPatientId());
        UserDetails employeeDetails = UserDetailsService.getInstance().getUserDetailsByID(appointment.getPatientId());

        request.setAttribute("prescription", prescription);
        request.setAttribute("patientDetails", patientDetails);
        request.setAttribute("employeeDetails", employeeDetails);
        request.setAttribute("employeeUserGroup", SystemUserService.getInstance().getUserByUserDetailsId(employeeDetails.getId()).getUserGroup());

        request.setAttribute("thirdPartys", ThirdPartyService.getInstance().getThirdParties());

        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentInProgress.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if( request.getParameter("selectedAppointmentId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/appointments/schedule"));
            return;
        }
        if(request.getParameter("editPrescription") != null) {
            String selectedAppointmentIdParam = "selectedAppointmentId=" + request.getParameter("selectedAppointmentId");
            int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
            Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);

            Prescription prescription = PrescriptionService.getInstance().getPrescriptionForAppointment(appointment.getId());

            response.sendRedirect(
                    UrlUtils.absoluteUrl(request, "/prescriptions/edit?selectedPrescriptionId=" + prescription.getId() + "&redirect=" +
                            UrlUtils.absoluteUrl(request, "/appointments/inprogress?" + selectedAppointmentIdParam)));
            return;
        }else if(request.getParameter("issuePrescription") != null){
            HttpSession session = request.getSession(false);
            FormData formData = new FormData(request.getParameter("notes"), Integer.parseInt(request.getParameter("referalId")));
            session.setAttribute("persistentFormData", formData);

            String selectedAppointmentIdParam = "selectedAppointmentId=" + request.getParameter("selectedAppointmentId");

            response.sendRedirect(
                    UrlUtils.absoluteUrl(request, "/prescriptions/issue?" + selectedAppointmentIdParam + "&redirect=" +
                    UrlUtils.absoluteUrl(request, "/appointments/inprogress?" + selectedAppointmentIdParam)));
            return;
        }else if(request.getParameter("completeAppointment") != null){

            int selectedAppointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));
            int selectedThirdParty = Integer.parseInt(request.getParameter("referalId"));

            Appointment appointment = AppointmentsService.getInstance().getAppointmentById(selectedAppointmentId);
            appointment.setStatus(AppointmentStatus.COMPLETE);
            appointment.setNotes(request.getParameter("notes"));

            if(selectedThirdParty != -1){
                appointment.setThirdPartyId(selectedThirdParty);
            }
            AppointmentsService.getInstance().updateAppointment(appointment);
            InvoiceService.getInstance().createInvoiceFromAppointment(appointment);


            String selectedAppointmentIdParam = "selectedAppointmentId=" + request.getParameter("selectedAppointmentId");
            response.sendRedirect(
                    UrlUtils.absoluteUrl(request, "/appointments/completed?" + selectedAppointmentIdParam));
            return;
        }
        doGet(request, response);
    }

    private class FormData{
        private String appointmentNotes;
        private Integer selectedReferal;

        public FormData(String appointmentNotes, Integer selectedReferal) {
            this.appointmentNotes = appointmentNotes;
            this.selectedReferal = selectedReferal;
        }

        public String getAppointmentNotes() {
            return appointmentNotes != null ? appointmentNotes : "";
        }

        public Integer getSelectedReferal() {
            return selectedReferal != null ? selectedReferal : -1;
        }
    }
}
