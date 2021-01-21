package com.esd.controller.pagecontrollers.workinghours;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.WorkingHours;
import com.esd.model.service.WorkingHoursService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.LocalTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/users/hours")
@Authentication(userGroups = {UserGroup.ADMIN})
public class WorkingHoursController extends HttpServlet {

    WorkingHoursService workingHoursService = WorkingHoursService.getInstance();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());
        request.setAttribute("pageTitle", "Manage Users");

        int userID = Integer.parseInt(request.getParameter("id"));

        List<WorkingHours> workingHours = workingHoursService.getWorkingHoursForEmployee(userID);

        request.setAttribute("workingHours", workingHours);
        RequestDispatcher view = request.getRequestDispatcher("/users/editWorkingHours.jsp");
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {


        LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
        LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));

        List<Integer> days = new ArrayList<>();

        if(request.getParameter("monday") != null) days.add(1);
        if(request.getParameter("tuesday") != null) days.add(2);
        if(request.getParameter("wednesday") != null) days.add(3);
        if(request.getParameter("thursday") != null) days.add(4);
        if(request.getParameter("friday") != null) days.add(5);
        if(request.getParameter("saturday") != null) days.add(6);
        if(request.getParameter("sunday") != null) days.add(7);

        WorkingHours workingHours = new WorkingHours(days, startTime, endTime);

        workingHours.setEmployeeDetailsId(Integer.parseInt(request.getParameter("id")));

        try {
            workingHoursService.addWorkingHoursToEmployee(workingHours);
            response.sendRedirect("manage?errMsg=Successfully updated working hours");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendRedirect("manage?errMsg=Error updating working hours");
        }
    }
}
