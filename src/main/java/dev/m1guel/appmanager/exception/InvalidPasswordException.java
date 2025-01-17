package dev.m1guel.appmanager.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String email) {
        super(String.format("Invalid password for user with email '%s'", email));
    }

}
