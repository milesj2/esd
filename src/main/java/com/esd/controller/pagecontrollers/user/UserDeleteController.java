package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.exceptions.InvalidIdValueException;
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
@WebServlet("/users/delete")
@Authentication(userGroups = {UserGroup.ADMIN})
public class UserDeleteController extends HttpServlet {

    private SystemUserService systemUserService = SystemUserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException
    {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        UUID uuid = UUID.randomUUID();
        session.setAttribute("deleteHash", uuid.toString());
        request.setAttribute("deleteHash", uuid.toString());

        request.setAttribute("pageTitle", "Delete User");


        try {
            SystemUser systemUser = systemUserService.getUserByID(Integer.parseInt(request.getParameter("userID")));
            request.setAttribute("user", systemUser);
        } catch (SQLException | InvalidIdValueException throwables) {
            throwables.printStackTrace();
        }


        RequestDispatcher view = request.getRequestDispatcher("/users/userDelete.jsp");
        view.forward(request, response);
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException
    {
        String requestUUID= request.getParameter("deleteHash");
        String sessionUUID = (String) request.getSession().getAttribute("deleteHash");

        request.getSession().removeAttribute("deleteHash");

        System.out.println("Received Hash: " + requestUUID + " | Stored Hash: " +  sessionUUID);
		
		if (requestUUID.equals(sessionUUID)){
            try {
                systemUserService.deleteUser(Integer.parseInt(request.getParameter("userID")));
            } catch (SQLException throwables) {
                response.sendRedirect("manage?errMsg=" + throwables.getMessage());
            }
            response.sendRedirect("manage?errMsg=" + "User deleted.");
		} else {
			response.sendRedirect("manage?errMsg=" + "Unauthorised delete request");
			return;
		}

		
		
	}
}
