package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.UserDetailsService;
import com.esd.model.service.SystemUserService;

import org.joda.time.LocalDate;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Original Author: Miles Jarvis
 * Use: The edit controller's use is to pass updated user details from editUserDetailsAndAccount.jsp's post data in SQL.
 *
 */
@WebServlet("/users/edit")
@Authentication(userGroups = {UserGroup.ALL})
public class UserEditController extends HttpServlet {

    private SystemUserService systemUserService = SystemUserService.getInstance();
    private UserDetailsService userDetailsService = UserDetailsService.getInstance();

    // todo filter as appropriate
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        SystemUser systemUser;

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        try {
            systemUser = systemUserService.getUserByID(Integer.parseInt(request.getParameter("id")));
            systemUser.setUserDetails(userDetailsService.getUserDetailsByUserID(systemUser.getId()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (InvalidIdValueException | NumberFormatException e){
            response.sendRedirect("manage?errMsg=" + e.getMessage());
            return;
        }
        request.setAttribute("editUser", systemUser);
        RequestDispatcher view = request.getRequestDispatcher("/users/editUserDetailsAndAccount.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        boolean active = false;
        if (request.getParameter(DaoConsts.SYSTEMUSER_ACTIVE) != null)
            active = true;

        SystemUser systemUser = new SystemUser(
                Integer.parseInt(request.getParameter(DaoConsts.ID)),
                request.getParameter(DaoConsts.SYSTEMUSER_USERNAME),
                request.getParameter(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(request.getParameter(DaoConsts.SYSTEMUSER_USERGROUP)),
                active
        );

        UserDetails userDetails = null;
        try {
            userDetails = new UserDetails(
                    -1,
                    systemUser.getId(),
                    request.getParameter(DaoConsts.USERDETAILS_FIRSTNAME),
                    request.getParameter(DaoConsts.USERDETAILS_LASTNAME),
                    request.getParameter(DaoConsts.USERDETAILS_ADDRESS1),
                    request.getParameter(DaoConsts.USERDETAILS_ADDRESS2),
                    request.getParameter(DaoConsts.USERDETAILS_ADDRESS3),
                    request.getParameter(DaoConsts.USERDETAILS_TOWN),
                    request.getParameter(DaoConsts.USERDETAILS_POSTCODE),
                    LocalDate.parse(request.getParameter(DaoConsts.USERDETAILS_DOB))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Try updating SQL
        try {
            boolean updateUserResult;
            boolean updateUserDetailsResult;

            updateUserResult = systemUserService.updateUser(systemUser);
            updateUserDetailsResult = userDetailsService.updateUserDetails(userDetails);

            if (updateUserResult && updateUserDetailsResult) {
                response.sendRedirect("manage?errMsg=Success");
            } else {
                response.sendRedirect("manage?errMsg=Error updating user or user details.");
            }

        } catch (SQLException e) {
            response.sendRedirect("manage?errMsg=" + e.getMessage());
            System.out.println(e.getMessage());
        } catch (InvalidIdValueException e) {
            response.sendRedirect("manage?errMsg=" + "Invalid id user'" + systemUser.getId() + "'");
            System.out.println(e.getMessage());
        }
    }
}
