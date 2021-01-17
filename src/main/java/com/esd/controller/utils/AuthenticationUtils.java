package com.esd.controller.utils;

import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtils {

    public static UserGroup getCurrentUserGroup(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("currentSessionUser");
        return user.getUserGroup();
    }
    public static User getCurrentUser(HttpServletRequest request){
        return (User)request.getSession().getAttribute("currentSessionUser");
    }
}
