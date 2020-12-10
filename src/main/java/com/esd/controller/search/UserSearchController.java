package com.esd.controller.search;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Trent meier
 * Use: the user search controller validates the user and then redirects to the user search
 * page
 */

@WebServlet("/userSearch")
public class UserSearchController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // Validate user is logged in
        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("../../index.jsp");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN){ //todo add user group validation
            response.sendRedirect("../../index.jsp");
            return;
        }

        try {
            response.sendRedirect("search/userSearch.jsp"); //logged-in page
        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("index.jsp?err=true"); //error page
        }
    }
}
