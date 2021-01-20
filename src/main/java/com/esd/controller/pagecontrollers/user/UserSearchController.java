package com.esd.controller.pagecontrollers.user;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.UserDetailsSearchRow;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.UserDetailsService;

import javax.servlet.annotation.WebServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        columns = Arrays.asList(
                new SearchColumn(DaoConsts.ID, "Id", "number"),
                new SearchColumn(DaoConsts.USERDETAILS_FIRSTNAME, "First name", "text"),
                new SearchColumn(DaoConsts.USERDETAILS_LASTNAME, "Last name", "text"),
                new SearchColumn(DaoConsts.USERDETAILS_ADDRESS1, "Address", "text"),
                new SearchColumn(DaoConsts.USERDETAILS_TOWN, "Town", "text"),
                new SearchColumn(DaoConsts.USERDETAILS_POSTCODE, "Post Code", "text"),
                new SearchColumn(DaoConsts.USERDETAILS_DOB, "Date of birth", "date")
        );
        selectedKey = "selectedUserId";
    }

    @Override
    public List<SearchRow> getSearchResults(SystemUser currentUser, Map<String, Object> args) {
        List<UserDetails> userDetails = userDetailsService.getUserDetailsFromFilteredRequest(currentUser, args);
        List<SearchRow> searchRows = new ArrayList<>();
        for(UserDetails userDetail : userDetails){
            searchRows.add(new UserDetailsSearchRow(userDetail));
        }
        return searchRows;
    }
}
