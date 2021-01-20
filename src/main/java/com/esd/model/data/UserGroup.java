package com.esd.model.data;

import java.util.*;

/**
 * Original Author: Jordan Hellier
 * Use: This enum is used for the type of user, its main use is for page restriction.
 */
public enum UserGroup {

    ADMIN,
    NHS_PATIENT,
    PRIVATE_PATIENT,
    DOCTOR,
    NURSE,
    RECEPTIONIST,
    //Used for user filtering
    ALL;

    public static List<UserGroup> patients = Arrays.asList(NHS_PATIENT, PRIVATE_PATIENT);

    public static Map<String, Object> getNavForUserGroup(UserGroup userGroup){

        Map<String, Object> navMap = new HashMap<>();
            Map<String, String> subNavList = new HashMap<>();
            subNavList.put("Book An Appointment", "/appointment/schedule");
            subNavList.put("View Appointments", "/appointment/view");
        navMap.put("Appointments", subNavList);
        navMap.put("Invoices", "/invoices/search");

        if(userGroup.equals(UserGroup.DOCTOR) || userGroup.equals(UserGroup.NURSE) ||
                userGroup.equals(UserGroup.ADMIN) || userGroup.equals(UserGroup.RECEPTIONIST)){
            subNavList.clear();
                subNavList.put("Search Users", "/users/search");
            navMap.put("Users", subNavList);
        }

        if(userGroup.equals(UserGroup.ADMIN) || userGroup.equals(UserGroup.ADMIN)){

        }

        return navMap;
    }
}
