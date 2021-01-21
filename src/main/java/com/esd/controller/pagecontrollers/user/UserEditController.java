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

        boolean doEditUser = true,
                doEditUserDetails = true,
                doUpdatePassword = false,
                editUserResult = true,
                editUserDetailsResult = true,
                updatePasswordResult = true;


        int userID;

        String referer = request.getHeader("referer");
        String redirect = "manage?";

        if (referer.contains("my-account")){
            doEditUser = false;
            redirect = "my-account?";
            userID = ((SystemUser)request.getSession().getAttribute("currentSessionUser")).getId();
            if (request.getParameter("password") != null){
                doUpdatePassword = true;
                doEditUserDetails = false;
            }
        } else {
            userID = Integer.parseInt(request.getParameter(DaoConsts.ID));
        }

        try {
            if (doUpdatePassword)
                updatePasswordResult = updateUserPassword(request, userID);
            if (doEditUser)
                editUserResult = editUser(request, userID);
            if (doEditUserDetails)
                editUserDetailsResult = editUserDetails(request, userID);
        } catch (SQLException e) {
            response.sendRedirect(redirect + "errMsg=" + e.getMessage());
            System.out.println(e.getMessage());
        } catch (InvalidIdValueException e) {
            response.sendRedirect(redirect + "errMsg=" + "Invalid id user'" + request.getParameter(DaoConsts.ID) + "'");
            System.out.println(e.getMessage());
        }

        if (!editUserResult){
            response.sendRedirect(redirect + "errMsg=Error updating user details.");
        }

        if (!editUserDetailsResult){
            response.sendRedirect(redirect + "errMsg=Error updating user.");
        }

        if (!updatePasswordResult){
            response.sendRedirect(redirect + "errMsg=Error Updating Password.");
        }

        response.sendRedirect(redirect + "errMsg=Success");

    }

    private boolean editUser(HttpServletRequest request, int userID) throws InvalidIdValueException, SQLException {

        boolean active = false;

        if (request.getParameter(DaoConsts.SYSTEMUSER_ACTIVE) != null)
            active = true;

        SystemUser systemUser = new SystemUser(
                userID,
                request.getParameter(DaoConsts.SYSTEMUSER_USERNAME),
                request.getParameter(DaoConsts.SYSTEMUSER_PASSWORD),
                UserGroup.valueOf(request.getParameter(DaoConsts.SYSTEMUSER_USERGROUP)),
                active
        );

        return systemUserService.updateUser(systemUser);

    }

    private boolean editUserDetails(HttpServletRequest request, int userID) throws InvalidIdValueException, SQLException {
        UserDetails userDetails = null;

        userDetails = new UserDetails(
                -1,
                userID,
                request.getParameter(DaoConsts.USERDETAILS_FIRSTNAME),
                request.getParameter(DaoConsts.USERDETAILS_LASTNAME),
                request.getParameter(DaoConsts.USERDETAILS_ADDRESS1),
                request.getParameter(DaoConsts.USERDETAILS_ADDRESS2),
                request.getParameter(DaoConsts.USERDETAILS_ADDRESS3),
                request.getParameter(DaoConsts.USERDETAILS_TOWN),
                request.getParameter(DaoConsts.USERDETAILS_POSTCODE),
                LocalDate.parse(request.getParameter(DaoConsts.USERDETAILS_DOB))
        );

        return userDetailsService.updateUserDetails(userDetails);
    }

    private boolean updateUserPassword(HttpServletRequest request, int userID) throws InvalidIdValueException, SQLException {
		SystemUser user = systemUserService.getUserByID(userID);

        if (user.getPassword().equals(request.getParameter("password")))
            return systemUserService.updatePassword(request.getParameter("new_password"), userID);
        else
            return false;
    }

}
