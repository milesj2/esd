package com.esd.controller.utils;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtils {

    public static UserGroup getCurrentUserGroup(HttpServletRequest request){
        SystemUser user = (SystemUser)request.getSession().getAttribute("currentSessionUser");
        return user.getUserGroup();
    }
    public static SystemUser getCurrentUser(HttpServletRequest request){
        return (SystemUser)request.getSession().getAttribute("currentSessionUser");
    }
}
