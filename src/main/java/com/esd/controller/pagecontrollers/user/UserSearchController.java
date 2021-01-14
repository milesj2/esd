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


    public UserSearchController() {
        formValues =  new ArrayList<>(Arrays.asList(
                DaoConsts.ID,
                DaoConsts.USERDETAILS_FIRSTNAME,
                DaoConsts.USERDETAILS_LASTNAME ,
                DaoConsts.USERDETAILS_ADDRESS1,
                DaoConsts.USERDETAILS_TOWN,
                DaoConsts.USERDETAILS_POSTCODE,
                DaoConsts.USERDETAILS_DOB));

        searchFilterFunction = userDetailsService::getUserDetailsFromFilteredRequest;
        searchPage = "userDetailsSearch.jsp";
        selectedKey = "selectedUserId";
    }



}
