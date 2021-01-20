package com.esd.controller.pagecontrollers.examples;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/example/persistent")
@Authentication(userGroups={UserGroup.ADMIN})
public class PersistentDataSearch  extends HttpServlet {

    private static final String CONFIRM = "confirm";
    private static final String SELECT_PATIENT = "selectPatient";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        //handle the search button being pressed.
        if(handleUserSearchRequest(request, response)){
            return;
        }

        //load form data if any from the session
        HttpSession session = request.getSession(true);
        FormData formData = (FormData) session.getAttribute("persistentFormData");

        //if our form data isn't null weve come back from a search
        if(formData != null){
            //load the search response
            handleSearchResponse(request, response, formData);

            //if we never actually set our usersearch response
            if(request.getAttribute("patientId") == null){
                request.setAttribute("patientId", formData.patientId);
            }
            request.setAttribute("dummyInput", formData.dummyInput);
            session.removeAttribute("persistentFormData");
        }

        //give the user the view
        RequestDispatcher view = request.getRequestDispatcher("/examples/persistent.jsp");
        view.forward(request, response);
    }

    private boolean handleUserSearchRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter(SELECT_PATIENT) != null){
            //save our form data in the session so we can retrieve it later
            String patientId = request.getParameter("patientId");
            String dummyInput = request.getParameter("dummyInput");

            HttpSession session = request.getSession(true);
            session.setAttribute("persistentFormData", new FormData(patientId, dummyInput, FormData.REQUEST_PATIENT_ID));
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/users/search?redirect=" + UrlUtils.absoluteUrl(request, "/example/persistent"))); //logged-in page
            return true;
        }
        return false;

    }

    private void handleSearchResponse(HttpServletRequest request, HttpServletResponse response, FormData formData) {
        if(request.getParameter("selectedUserId") != null && formData.requestedProperty.equals(FormData.REQUEST_PATIENT_ID)){
            request.setAttribute("patientId", request.getParameter("selectedUserId"));
        }
    }


    private class FormData {
        private static final String REQUEST_PATIENT_ID = "PATIENTID";

        private final String patientId;
        private final String dummyInput;
        private final String requestedProperty;

        public FormData(String patientId, String dummyInput, String requestedProperty) {
            this.patientId = patientId;
            this.dummyInput = dummyInput;
            this.requestedProperty = requestedProperty;
        }
    }
}