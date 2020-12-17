package com.esd.controller.authentication;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.exceptions.InvalidUserCredentialsException;
import com.esd.model.service.UserService;
import com.esd.views.LoginErrors;
import com.esd.views.ViewsConsts;

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

            if (!user.isActive()){
                response.sendRedirect("login?err=" + LoginErrors.AccountDisabled);
                return;
            }
            //create http session
            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser",user);
            response.sendRedirect("dashboard"); //logged-in page
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.sendRedirect("login?err=" + LoginErrors.Unknown);
        } catch (InvalidUserCredentialsException e){
            System.out.println(e.getMessage());
            response.sendRedirect("login?err=" + LoginErrors.IncorrectCredentials);
        }
    }
}
