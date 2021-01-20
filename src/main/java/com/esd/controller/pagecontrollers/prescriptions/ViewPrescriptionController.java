package com.esd.controller.pagecontrollers.prescriptions;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Prescription;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.PrescriptionService;
import com.esd.model.service.UserDetailsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/prescriptions/view")
@Authentication(userGroups = {UserGroup.ALL})
public class ViewPrescriptionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("selectedPrescriptionId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/dashboard"));
            return;
        }

        request.setAttribute("pageTitle", "Prescriptions");

        int prescriptionId = Integer.parseInt(request.getParameter("selectedPrescriptionId"));
        Prescription prescription = PrescriptionService.getInstance().getPrescriptionById(prescriptionId);
        UserDetails currentUserDetails = UserDetailsService.getInstance().getUserDetailsByUserID(AuthenticationUtils.getCurrentUser(request).getId());

        if(UserGroup.patients.contains(AuthenticationUtils.getCurrentUserGroup(request))){
            if(currentUserDetails.getId() != prescription.getPatientId()){
                response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_FORBIDDEN));
                return;
            }
        }

        UserDetails issuedToDetails = UserDetailsService.getInstance().getUserDetailsByID(prescription.getPatientId());
        UserDetails issuedByDetails = UserDetailsService.getInstance().getUserDetailsByID(prescription.getEmployeeId());
        request.setAttribute("issuedToDetails", issuedToDetails);
        request.setAttribute("issuedByDetails", issuedByDetails);
        request.setAttribute("prescription", prescription);

        List<Prescription> childPrescriptions = PrescriptionService.getInstance().getChildPrescriptionsByPrescriptionId(prescriptionId);
        request.setAttribute("childPrescriptions", childPrescriptions);
        RequestDispatcher view = request.getRequestDispatcher("/prescriptions/viewPrescription.jsp");
        view.forward(request, response);
    }
}
