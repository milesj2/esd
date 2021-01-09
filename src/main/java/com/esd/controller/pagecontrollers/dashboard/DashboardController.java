package com.esd.controller.pagecontrollers.dashboard;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
@Authentication(userGroups = {UserGroup.ALL})
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException
    {
        request.setAttribute("pageTitle", "Welcome");
        request.setAttribute("previousPage", request.getAttribute("currentPage"));
        request.setAttribute("currentPage", "dashboard");

        RequestDispatcher view = request.getRequestDispatcher("/dashboard.jsp");
        view.forward(request, response);
    }
}
