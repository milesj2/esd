package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.service.UserDetailsService;

import javax.servlet.annotation.WebServlet;

import java.util.ArrayList;
import java.util.Arrays;

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
