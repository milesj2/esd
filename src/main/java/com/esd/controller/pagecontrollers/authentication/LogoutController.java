package com.esd.controller.pagecontrollers.authentication;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;

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
@Authentication(userGroups = {UserGroup.ALL})
public class LogoutController extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
            response.sendRedirect(UrlUtils.absoluteUrl(request, "/login"));
        } catch (Throwable theException) {
            theException.printStackTrace();
            throw theException;
        }
    }
}
