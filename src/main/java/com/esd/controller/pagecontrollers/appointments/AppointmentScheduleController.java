package com.esd.controller.pagecontrollers.appointments;


import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UIAppointment;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/appointments/schedule")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentScheduleController extends HttpServlet {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    private SystemUserService systemUserService = SystemUserService.getInstance();
    private UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        request.setAttribute("pageTitle", "Schedule");

        SystemUser systemUser = (SystemUser)request.getSession().getAttribute("currentSessionUser");

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
            List<Appointment> appointmentList;
            if (request.getParameter("employeeID") != null){
                appointmentList = appointmentsService.getEmployeeAppointments(fromDate, toDate, Integer.parseInt(request.getParameter("employeeID")));
            } else if (request.getParameter("patientID") != null){
                appointmentList = appointmentsService.getPatientsAppointments(fromDate, toDate, Integer.parseInt(request.getParameter("patientID")));
            } else if (systemUser.getUserGroup() == UserGroup.PRIVATE_PATIENT ||
                    systemUser.getUserGroup() == UserGroup.NHS_PATIENT){
                appointmentList = appointmentsService.getPatientsAppointments(fromDate, toDate, systemUser.getId());
            } else if (systemUser.getUserGroup() == UserGroup.ADMIN){
                appointmentList = appointmentsService.getAllAppointments(fromDate, toDate);
            }
            else {
                appointmentList = appointmentsService.getEmployeeAppointments(fromDate, toDate, systemUser.getId());
            }

            for (Appointment appointment: appointmentList){
                UIAppointment uiAppointment = new UIAppointment();

                SystemUser employee = systemUserService.getUserByID(appointment.getEmployeeId());
                SystemUser patient = systemUserService.getUserByID(appointment.getPatientId());
                patient.setUserDetails(userDetailsService.getUserDetailsByUserID(patient.getId()));
                employee.setUserDetails(userDetailsService.getUserDetailsByUserID(employee.getId()));

                StringBuilder title = new StringBuilder();
                title.append(patient.getUserDetails().getFirstName());
                title.append(" ");
                title.append(patient.getUserDetails().getLastName());
                title.append(" to see ");
                title.append(employee.getUserGroup().toString().toLowerCase());
                title.append(" ");
                title.append(employee.getUserDetails().getLastName());

                uiAppointment.setId(appointment.getId());
                uiAppointment.setTitle(title.toString());
                uiAppointment.setPatient(patient);
                uiAppointment.setEmployee(employee);
                uiAppointment.setSlots(appointment.getSlots());
                uiAppointment.setAppointmentDate(appointment.getAppointmentDate());
                uiAppointment.setAppointmentTime(appointment.getAppointmentTime());
                uiAppointment.setStatus(appointment.getStatus());

                appointments.add(uiAppointment);
            }
        } catch (SQLException | InvalidIdValueException throwables) {
            throwables.printStackTrace();
        }

        // set appointment list appropriate for user
        request.setAttribute("appointments", appointments);


        RequestDispatcher view = request.getRequestDispatcher("/appointments/viewAppointmentSchedule.jsp");
        view.forward(request, response);
    }
}
