package com.esd.model.exceptions;

public class InvalidUserDetailsIDException extends Exception {

    public static final String DEFAULT_MESSAGE = "No user details found for user id '%d'";

    public InvalidUserDetailsIDException(String message) {
        super(message);
    }
}
