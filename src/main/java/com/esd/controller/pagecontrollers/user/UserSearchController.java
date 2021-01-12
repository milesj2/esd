package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.UserDetailsService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Original Author: Trent meier
 * Use: the user search controller validates the user and then redirects to the user search
 * page
 */
@WebServlet("/users/search")
@Authentication(userGroups = {UserGroup.ALL})
public class UserSearchController extends GenericSearchController {

    private UserDetailsService userDetailsService = UserDetailsService.getInstance();
    private ArrayList<String> formValues =  new ArrayList<String>(Arrays.asList(
            DaoConsts.ID,
            DaoConsts.USERDETAILS_FIRSTNAME,
            DaoConsts.USERDETAILS_LASTNAME ,
            DaoConsts.USERDETAILS_ADDRESS1,
            DaoConsts.USERDETAILS_TOWN,
            DaoConsts.USERDETAILS_POSTCODE,
            DaoConsts.USERDETAILS_DOB));

    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) &&
                !request.getParameter(key).isEmpty() &&
                request.getParameter(key) != ""){
            return true;
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException
    {
        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());

        // Validate user is logged in
        User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
        if(currentUser == null){
            response.sendRedirect("login");
            return;
        } else if (currentUser.getUserGroup() != UserGroup.ADMIN){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        RequestDispatcher view = request.getRequestDispatcher("userDetailsSearch.jsp");
        view.forward(request, response);
    }

    protected void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException {

        URIBuilder redirectURIBuilder = null;
        try {
            redirectURIBuilder = new URIBuilder(request.getParameter("redirect"));

            if(request.getParameter("selectedUserId") != null) {
                redirectURIBuilder.addParameter("selectedUserId", request.getParameter("selectedUserId"));
            }

            response.sendRedirect(redirectURIBuilder.build().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void performSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> args =  new HashMap<>();
        for(String key: formValues) {
            if(checkRequestContains(request, key)){
                args.put(key, request.getParameter(key));
            }
        }
        try {
            // pass request with form keys and request (has post values)
            ArrayList<UserDetails> userDetailsList = userDetailsService.getUserDetailsFromFilteredRequest(args);
            request.setAttribute("table", userDetailsList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("userDetailsSearch.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
