package com.esd.views;

public enum LoginErrors {
    UNKNOWN("Unknown Error! Please contact admin if this error continues"),
    INCORRECT_CREDENTIALS("Invalid username/password"),
    ACCOUNT_DISABLED("This account is locked. Please contact your admin to reactive the account.");

    private String message;

    LoginErrors(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}