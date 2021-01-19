package com.esd.controller.pagecontrollers.prescriptions;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.PrescriptionRepeat;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.PrescriptionService;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@WebServlet("/prescriptions/issue")
@Authentication(userGroups = {UserGroup.DOCTOR, UserGroup.NURSE})
public class IssuePrescriptionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("/prescriptions/issuePrescription.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String prescriptionDetails = request.getParameter("details");
        int appointmentId = Integer.parseInt(request.getParameter("selectedAppointmentId"));

        Appointment appointment = AppointmentsService.getInstance()
                .getAppointmentById(appointmentId);
        int employeeId = appointment.getEmployeeId();
        int patientId = appointment.getPatientId();

        LocalDate issueDate = new LocalDate(request.getParameter("issueDate"));
        LocalDate repeatUntil = new LocalDate(request.getParameter("repeatUntil"));

        PrescriptionRepeat repeat = PrescriptionRepeat.valueOf(request.getParameter("repeat"));

        Prescription prescription = new Prescription();
        prescription.setPrescriptionDetails(prescriptionDetails);
        prescription.setAppointmentId(appointmentId);
        prescription.setIssueDate(issueDate);
        prescription.setEmployeeId(employeeId);
        prescription.setPatientId(patientId);

        PrescriptionService.getInstance().createPrescription(prescription);
        PrescriptionService.getInstance().repeatPrescription(prescription, repeat, repeatUntil);

        //TODO create prescription
        if(request.getParameter("redirect") != null){
            URIBuilder redirectURIBuilder = null;
            try {
                redirectURIBuilder = new URIBuilder(request.getParameter("redirect"));
                response.sendRedirect(redirectURIBuilder.build().toString());
                return;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(UrlUtils.absoluteUrl(request, "/dashboard"));
    }
}
