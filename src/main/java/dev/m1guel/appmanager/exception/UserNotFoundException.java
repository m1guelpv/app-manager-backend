package dev.m1guel.appmanager.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super(String.format("User with email '%s' not found", email));
    }

}
