package com.esd.controller.search;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.UserDetailsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Original Author: Trent meier
 * Use: the user search controller validates the user and then redirects to the user search
 * page
 */

@WebServlet("/userSearch")
public class UserSearchController extends HttpServlet {

    //corresponds to user search form
    private ArrayList<String> formValues =  new ArrayList<String>(Arrays.asList(
            DaoConsts.USERDETAILS_ID,
            DaoConsts.USERDETAILS_FIRSTNAME,
            DaoConsts.USERDETAILS_LASTNAME ,
            DaoConsts.USERDETAILS_ADDRESS1,
            DaoConsts.USERDETAILS_TOWN,
            DaoConsts.USERDETAILS_POSTCODE,
            DaoConsts.USERDETAILS_DOB));

    // serves page initially
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

    //returns search form data
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // pass request with form keys and request (has post values)
        ArrayList<UserDetails> userDetailsList = UserDetailsService.getInstance().getUserDetailsFromFilteredRequest(formValues, request);

        //return user details list
        request.setAttribute("table", userDetailsList);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/userSearch.jsp");
        requestDispatcher.forward(request, response);
    }
}
