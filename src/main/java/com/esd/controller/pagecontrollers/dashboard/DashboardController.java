package com.esd.controller.pagecontrollers.dashboard;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.Navigation;
import com.esd.model.data.UserGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        RequestDispatcher view = request.getRequestDispatcher("/dashboard.jsp");
        view.forward(request, response);
    }
}
