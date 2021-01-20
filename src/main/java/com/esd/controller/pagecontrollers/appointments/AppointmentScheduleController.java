package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.AppointmentPlaceHolder;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.SystemUserService;
import com.esd.model.service.UserDetailsService;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/appointments/schedule")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentScheduleController extends HttpServlet {

    public static String ATTRIBUTE_SELECTED_DATE = "selectedDate";
    public static String ATTRIBUTE_SELECTED_SPAN = "selectedSpan";
    public static String ATTRIBUTE_APPOINTMENT_MAP = "appointments";

    public static String ATTRIBUTE_ENTIRE_SCHEDULE = "entire";

    public static String ATTRIBUTE_SELECT_USER = "selectUser";
    public static String ATTRIBUTE_SELECTED_USER = "selectedUserId";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        request.setAttribute(ATTRIBUTE_SELECTED_DATE, new LocalDate());
        request.setAttribute(ATTRIBUTE_SELECTED_SPAN, 1);
        request.setAttribute(ATTRIBUTE_ENTIRE_SCHEDULE, null);

        SystemUser currentUser = AuthenticationUtils.getCurrentUser(request);
        if(currentUser.getUserDetails() == null){
            currentUser.setUserDetails(UserDetailsService.getInstance().getUserDetailsByUserID(currentUser.getId()));
        }

        if(request.getParameter(ATTRIBUTE_SELECTED_USER) != null){
            request.setAttribute(ATTRIBUTE_SELECTED_USER, request.getParameter(ATTRIBUTE_SELECTED_USER));
        }else if(currentUser.getUserGroup() == UserGroup.ADMIN){
            request.setAttribute(ATTRIBUTE_ENTIRE_SCHEDULE, null);
        }
        HashMap<LocalDate, List<Appointment>> data = getData(request);
        request.setAttribute(ATTRIBUTE_APPOINTMENT_MAP, data);

        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentSchedule.jsp");
        view.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter(ATTRIBUTE_SELECT_USER) != null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/users/search?redirect=" + UrlUtils.absoluteUrl(request, "/appointments/schedule")));
            return;
        }
        HashMap<LocalDate, List<Appointment>> data = getData(request);

        request.setAttribute(ATTRIBUTE_APPOINTMENT_MAP, data);
        RequestDispatcher view = request.getRequestDispatcher("/appointments/appointmentSchedule.jsp");
        view.forward(request, response);
    }

    public String getValue(HttpServletRequest request, String attribute){
        if(request.getMethod() == "GET"){
            Object value = request.getAttribute(attribute);
            return value == null ? null : value.toString();
        }
        return request.getParameter(attribute);
    }

    public HashMap<LocalDate,List<Appointment>> getData(HttpServletRequest request){
        SystemUser currentUser = AuthenticationUtils.getCurrentUser(request);
        if(currentUser.getUserDetails() == null){
            currentUser.setUserDetails(UserDetailsService.getInstance().getUserDetailsByUserID(currentUser.getId()));
        }

        LocalDate selectedDate = LocalDate.parse(getValue(request, ATTRIBUTE_SELECTED_DATE));
        int selectedSpan = Integer.parseInt(getValue(request,ATTRIBUTE_SELECTED_SPAN));

        request.setAttribute(ATTRIBUTE_SELECTED_DATE, selectedDate);
        request.setAttribute(ATTRIBUTE_SELECTED_SPAN, selectedSpan);

        HashMap<LocalDate, List<Appointment>> data;

        if(UserGroup.patients.contains(currentUser.getUserGroup())){
            data = AppointmentsService.getInstance().getAppointmentsInPeriodForPatientByUserDetailsId(currentUser.getId(), selectedDate, selectedSpan);
        }else{

            boolean entireSchedule = getValue(request,ATTRIBUTE_ENTIRE_SCHEDULE) != null;
            if(entireSchedule){
                request.setAttribute(ATTRIBUTE_ENTIRE_SCHEDULE, Boolean.valueOf(true));
            }else{
                request.setAttribute(ATTRIBUTE_ENTIRE_SCHEDULE, null);
            }
            int selectedUserId = 0;

            if(getValue(request,ATTRIBUTE_SELECTED_USER) != null){
                selectedUserId = Integer.parseInt(getValue(request,ATTRIBUTE_SELECTED_USER));
                request.setAttribute(ATTRIBUTE_SELECTED_USER, selectedUserId);
            }

            if(entireSchedule){
                data = AppointmentsService.getInstance().getAppointmentsInPeriod(selectedDate, selectedSpan);
            }else if(selectedUserId != 0){
                SystemUser selectedUser = SystemUserService.getInstance().getUserByUserDetailsId(selectedUserId);
                if(selectedUser.getUserDetails() == null){
                    selectedUser.setUserDetails(UserDetailsService.getInstance().getUserDetailsByUserID(selectedUser.getId()));
                }
                if(UserGroup.patients.contains(selectedUser.getUserGroup())){
                    data = AppointmentsService.getInstance().getAppointmentsInPeriodForPatientByUserDetailsId(selectedUser.getUserDetails().getUserId(), selectedDate, selectedSpan);
                }else{
                    data = AppointmentsService.getInstance().getAppointmentsInPeriodForEmployeeByUserDetailsId(selectedUser.getUserDetails().getUserId(), selectedDate, selectedSpan);
                }
            }else{
                data = AppointmentsService.getInstance().getAppointmentsInPeriodForEmployeeByUserDetailsId(currentUser.getUserDetails().getUserId(), selectedDate, selectedSpan);

            }
        }
        return data;
    }
}
