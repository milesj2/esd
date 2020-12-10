package com.esd.model.exceptions;

public class InvalidUserIDException extends Exception {

    public static final String DEFAULT_MESSAGE = "No user found for id '%d'";

    public InvalidUserIDException(String message) {
        super(message);
    }
}
