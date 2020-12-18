package com.esd.controller.pagecontrollers.authentication;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InactiveAccountException;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.service.UserService;
import com.esd.views.LoginErrors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Original Author: Jordan Hellier
 * Use: The login controller's use is to validate user credentials and redirect them to their
 * dashboard page.
 *
 */
@WebServlet("/login")
@Authentication(loggedInUserAccess=false, authenticationRequired = false)
public class LoginController extends HttpServlet{

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        try {
            User user = UserService.getInstance()
                    .validateCredentials(request.getParameter("username"), request.getParameter("password"));

            //create http session
            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser",user);
            response.sendRedirect(UrlUtils.absoluteUrl(request, "dashboard")); //logged-in page
            return;
        } catch (InvalidUserCredentialsException e){
            e.printStackTrace();
            request.setAttribute("errorMessage", LoginErrors.INCORRECT_CREDENTIALS.getMessage());
        } catch (InactiveAccountException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", LoginErrors.ACCOUNT_DISABLED.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", LoginErrors.UNKNOWN.getMessage());
        }

        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }
}
