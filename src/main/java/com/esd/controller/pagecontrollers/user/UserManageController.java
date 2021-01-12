package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Original Author: Miles Jarvis
 * Use: The edit controller's use is to pass updated user details from editUserDetailsAndAccount.jsp's post data in SQL.
 */
@WebServlet("/users/manage")
@Authentication(userGroups = {UserGroup.ADMIN})
public class UserManageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException
    {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        request.setAttribute("pageTitle", "Manage Users");

        List<User> users;

        try {
            users = UserService.getInstance().getUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("users", users);
        RequestDispatcher view = request.getRequestDispatcher("/users/manageUserAccount.jsp");
        view.forward(request, response);
    }
}
