package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.UserDetails;

import java.util.Arrays;
import java.util.HashMap;

public class UserDetailsSearchRow extends SearchRow {

    public UserDetailsSearchRow(UserDetails userDetail) {
        this.id = userDetail.getId();
        this.columns = Arrays.asList(
                String.valueOf(userDetail.getId()),
                userDetail.getFirstName(),
                userDetail.getLastName(),
                userDetail.getAddressLine1(),
                userDetail.getTown(),
                userDetail.getPostCode(),
                userDetail.getDateOfBirth().toString()
        );
        searchActions = new HashMap<>();
        searchActions.put("/users/edit?id="+userDetail.getId(), "View User Details");
    }
}
