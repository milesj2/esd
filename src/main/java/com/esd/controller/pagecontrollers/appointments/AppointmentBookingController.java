package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.AppointmentPlaceHolder;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.UserDetailsService;
import com.esd.model.service.UserService;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/appointments/book")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentBookingController extends HttpServlet {

    public static final String ATTRIBUTE_SELECTED_DATE = "selectedDate";
    public static final String ATTRIBUTE_AVAILABLE_APPOINTMENTS = "availableAppointments";
    public static final String ATTRIBUTE_AVAILABLE_APPOINTMENTS_DOCTOR = "availableAppointmentsDoctor";
    public static final String ATTRIBUTE_AVAILABLE_APPOINTMENTS_NURSE = "availableAppointmentsNurse";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserGroup usergroup = AuthenticationUtils.getCurrentUserGroup(request);

        //We need a patient ID to start the booking process.
        int patientId = -1;
        if(!UserGroup.patients.contains(usergroup)){
            if( request.getParameter("selectedUserId") == null){
                response.sendRedirect(UrlUtils.absoluteUrl(request, "/users/search?redirect=" + UrlUtils.absoluteUrl(request, "/appointments/book")));
                return;
            }
            patientId = Integer.parseInt(request.getParameter("selectedUserId"));
        }else{
            User user = AuthenticationUtils.getCurrentUser(request);
            if(user.getUserDetails() == null){
                    UserDetails details = UserDetailsService.getInstance().getUserDetailsByUserID(user.getId());
                    patientId = details.getId();
            }
        }

        if(patientId == -1){
            UrlUtils.error(request, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        //next step is to select the appointment date
        LocalDate date;

        if(request.getParameter(ATTRIBUTE_SELECTED_DATE) != null){
            date = new LocalDate(request.getParameter(ATTRIBUTE_SELECTED_DATE));
        }else{
            date = new LocalDate();
        }

        request.setAttribute(ATTRIBUTE_SELECTED_DATE, date);

        Map<Integer, List<AppointmentPlaceHolder>> placeHolderList = AppointmentsService.getInstance().generateAllPossibleAppointments(date, 1);

        Map<Integer, List<AppointmentPlaceHolder>> doctorAppointmentsList = new HashMap<>();
        Map<Integer, List<AppointmentPlaceHolder>> nurseAppointmentsList = new HashMap<>();

        request.setAttribute(ATTRIBUTE_AVAILABLE_APPOINTMENTS, placeHolderList);

        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentBooking.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
