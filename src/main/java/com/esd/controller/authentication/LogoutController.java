package com.esd.controller.authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Original Author: Jordan Hellier
 * Use: The logout controller's use is to invalidate the current user's session and redirect them to the index page.
 *
 */
@WebServlet("/logout")
public class LogoutController extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
            response.sendRedirect("index.jsp");
        } catch (Throwable theException) {
            System.out.println(theException);
            throw theException;
        }
    }


}
