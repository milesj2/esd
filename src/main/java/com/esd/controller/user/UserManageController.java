package com.esd.controller.user;

import com.esd.model.data.persisted.User;
import com.esd.model.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Original Author: Miles Jarvis
 * Use: The edit controller's use is to pass updated user details from edit.jsp's post data in SQL.
 */
@WebServlet("/user/manage")
public class UserManageController extends HttpServlet {

    // TODO Needs filter
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        List<User> users;

        try {
            users = UserService.getInstance().getUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("users", users);
        RequestDispatcher view = request.getRequestDispatcher("/admin/users/manage.jsp");
        view.forward(request, response);
    }
}
