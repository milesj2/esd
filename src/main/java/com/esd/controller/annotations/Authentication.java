package com.esd.controller.annotations;

import com.esd.model.data.UserGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

    boolean authenticationRequired() default true;
    boolean loggedInUserAccess() default true;
    UserGroup[] userGroups() default {};
}
