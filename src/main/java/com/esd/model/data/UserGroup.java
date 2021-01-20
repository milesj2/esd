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
}
