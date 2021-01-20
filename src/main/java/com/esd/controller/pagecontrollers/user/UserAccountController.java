package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.SystemUserService;
import com.esd.model.service.UserDetailsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/users/my-account")
@Authentication(userGroups = {UserGroup.ALL})
public class UserAccountController extends HttpServlet {

    private final SystemUserService userService = SystemUserService.getInstance();
    private final UserDetailsService userDetailsService = UserDetailsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        SystemUser currentUser = (SystemUser)request.getSession().getAttribute("currentSessionUser");
        SystemUser user;

        try {
            user = userService.getUserByID(currentUser.getId());
            user.setUserDetails(userDetailsService.getUserDetailsByUserID(user.getId()));
            request.setAttribute("editUser", user);
        } catch (SQLException | InvalidIdValueException throwables) {
            throwables.printStackTrace();
        }

        request.setAttribute("pageTitle", "My Account");
        RequestDispatcher view = request.getRequestDispatcher("/users/userAccountEdit.jsp");
        view.forward(request, response);
    }
}
