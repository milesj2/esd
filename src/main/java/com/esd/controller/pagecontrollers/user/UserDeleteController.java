package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.SystemUserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Original Author: Miles Jarvis
 * Use: 
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
        request.setAttribute("pageTitle", "Delete User");

        session.setAttribute("deleteHash", UUID.randomUUID().toString());

        List<SystemUser> systemUsers;

        request.setAttribute("users", systemUsers);
        RequestDispatcher view = request.getRequestDispatcher("/users/manageUserAccount.jsp");
        view.forward(request, response);
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException
    {
		
		if (request.getAttribute("deleteHash") == session.getAttribute("deleteHash")){
			// delete 
		} else {
			response.sendRedirect("manage?errMsg=" + "Unauthorised delete request");
			return;
		}
		response.sendRedirect("manage?errMsg=" + "User deleted.");
		
		
	}
}
