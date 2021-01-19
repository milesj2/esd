package com.esd.controller.pagecontrollers.prescriptions;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.service.PrescriptionService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@WebServlet("/prescriptions/edit")
@Authentication(userGroups = {UserGroup.DOCTOR, UserGroup.NURSE})
public class EditPrescriptionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("selectedPrescriptionId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/dashboard"));
            return;
        }
        int prescriptionId = Integer.parseInt(request.getParameter("selectedPrescriptionId"));
        Prescription prescription = PrescriptionService.getInstance().getPrescriptionById(prescriptionId);
        request.setAttribute("details", prescription.getPrescriptionDetails());

        RequestDispatcher view = request.getRequestDispatcher("/prescriptions/editPrescription.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("cancel") == null) {
            String prescriptionDetails = request.getParameter("details");
            int prescriptionId = Integer.parseInt(request.getParameter("selectedPrescriptionId"));

            Prescription prescription = PrescriptionService.getInstance().getPrescriptionById(prescriptionId);
            prescription.setPrescriptionDetails(prescriptionDetails);
            PrescriptionService.getInstance().updatePrescription(prescription);
        }
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
