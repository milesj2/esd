package com.esd.controller.authentication;

import com.esd.model.data.User;
import com.esd.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/login")
public class LoginController extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try
        {
            User user = UserService.getInstance().validateCredentials(request.getParameter("username"), request.getParameter("password"));

            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser",user);
            response.sendRedirect("index.jsp"); //logged-in page
        }
        catch (Exception e)
        {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
}
