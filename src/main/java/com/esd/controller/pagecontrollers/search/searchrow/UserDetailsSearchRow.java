package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.SystemUserService;

import java.util.Arrays;
import java.util.HashMap;

public class UserDetailsSearchRow extends SearchRow {

    public UserDetailsSearchRow(UserDetails userDetail) {
        SystemUser systemUser = SystemUserService.getInstance().getUserByUserDetailsId(userDetail.getId());
        this.id = userDetail.getId();
        this.columns = Arrays.asList(
                String.valueOf(userDetail.getId()),
                userDetail.getFirstName(),
                userDetail.getLastName(),
                userDetail.getAddressLine1(),
                userDetail.getTown(),
                userDetail.getPostCode(),
                userDetail.getDateOfBirth().toString(),
                systemUser.getUserGroup().toString()
        );
        searchActions = new HashMap<>();
        searchActions.put("/users/edit?id="+userDetail.getId(), "View User Details");
    }
}
