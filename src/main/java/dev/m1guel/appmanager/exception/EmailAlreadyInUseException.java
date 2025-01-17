package dev.m1guel.appmanager.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super(String.format("The email '%s' is already in use", email));
    }

}