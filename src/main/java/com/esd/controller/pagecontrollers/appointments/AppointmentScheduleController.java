package com.esd.controller.pagecontrollers.appointments;


import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UIAppointment;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.AppointmentsService;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/appointments/schedule")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentScheduleController extends HttpServlet {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    private static final ArrayList<String> AppointmentKeys = new ArrayList<>(Arrays.asList(
            DaoConsts.APPOINTMENT_SLOTS,
            DaoConsts.APPOINTMENT_STATUS,
            DaoConsts.ID)
    );

    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) && !request.getParameter(key).isEmpty() && request.getParameter(key) != ""){
            return true;
        }
        return false;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        Map<String, Object> args =  new HashMap<>();

        SystemUser systemUser = (SystemUser)request.getSession().getAttribute("currentSessionUser");

        if (systemUser.getUserGroup() == UserGroup.NHS_PATIENT
                || systemUser.getUserGroup() == UserGroup.PRIVATE_PATIENT
                || !checkRequestContains(request, "id"))
        {
            args.put("id", systemUser.getId());
        } else {
            for(String key: AppointmentKeys) {
                if(checkRequestContains(request, key)){
                    args.put(key, request.getParameter(key));
                }
            }
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        LocalDate fromDate;
        LocalDate toDate;

        fromDate = LocalDate.parse(request.getParameter("fromDate"));
        try {
            c.setTime(sdf.parse(request.getParameter("fromDate")));
        } catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 7);
        toDate = LocalDate.parse(sdf.format(c.getTime()));

        ArrayList<UIAppointment> appointments = new ArrayList<>();

        try {
            List<Appointment> appointmentList = appointmentsService.getAppointmentsInRange(fromDate, toDate, Optional.ofNullable(args));
            for (Appointment appointment: appointmentList){
                UIAppointment uiAppointment = new UIAppointment();

                uiAppointment.setId(appointment.getId());
                uiAppointment.setTitle(String.valueOf(appointment.getPatientId()));
                uiAppointment.setSlots(appointment.getSlots());
                uiAppointment.setAppointmentDate(appointment.getAppointmentDate());
                uiAppointment.setAppointmentTime(appointment.getAppointmentTime());
                uiAppointment.setStatus(appointment.getStatus());

                appointments.add(uiAppointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // set appointment list appropriate for user
        request.setAttribute("appointments", appointments);


        RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointmentSchedule.jsp");
        view.forward(request, response);
    }
}
