package com.esd.model.exceptions;

/**
 * Original Author: Jordan Hellier
 * Use: Thrown when a user authentication fails: IE: no username found or incorrect password
 */
public class InvalidUserCredentialsException extends Exception {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }
}
