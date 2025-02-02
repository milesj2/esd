package com.esd.controller.pagecontrollers.dashboard;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dashboard")
@Authentication(userGroups = {UserGroup.ALL})
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException
    {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        request.setAttribute("pageTitle", "Welcome");
        populateDashboardWidgets(request);

        RequestDispatcher view = request.getRequestDispatcher("/dashboard.jsp");
        view.forward(request, response);
    }

    private void populateDashboardWidgets(HttpServletRequest request) {
        List<DashboardWidget> widgets = new ArrayList<>();

        SystemUser currentUser = AuthenticationUtils.getCurrentUser(request);

        widgets.addAll(DashboardWidget.COMMON_WIDGETS);

        if(UserGroup.employees.contains(currentUser.getUserGroup())) {
            widgets.addAll(DashboardWidget.COMMON_EMPLOYEE_WIDGETS);
        }else if(UserGroup.patients.contains(currentUser.getUserGroup())){
            widgets.addAll(DashboardWidget.PATIENT_WIDGETS);
        }

        if(UserGroup.ADMIN == currentUser.getUserGroup()){
            widgets.addAll(DashboardWidget.ADMIN_WIDGETS);
        }

        request.setAttribute("widgets", widgets);
    }
}
