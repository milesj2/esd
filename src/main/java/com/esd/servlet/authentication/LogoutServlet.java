package com.esd.servlet.authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try
        {
            HttpSession session = request.getSession(false);
            session.invalidate();
            response.sendRedirect("index.jsp");

        }
        catch (Throwable theException)
        {
            System.out.println(theException);
            throw theException;
        }
    }


}
