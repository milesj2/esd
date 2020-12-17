package com.esd.controller.dashboard;

import com.esd.model.data.persisted.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException {


        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("login");
            return;
        }

        RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
        view.forward(request, response);
    }
}
