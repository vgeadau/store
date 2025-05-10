package com.example.store.util;

/**
 * Utility class that holds all the error messages.
 */
public final class ErrorMessages {

    /**
     * private constructor specific to utility classes.
     */
    private ErrorMessages() {
        throw new UnsupportedOperationException("You are not allowed to extend or instantiate this utility class");
    }

    /**
     * {@link com.example.store.service.AuthService} error messages.
     */
    public static final String AUTHENTICATION_ERROR = "Invalid username or password";

    /**
     * {@link com.example.store.service.MyUserDetailsService} error messages.
     */
    public static final String NOT_FOUND = "Not found: ";

    /**
     * {@link com.example.store.service.ValidationService} error messages.
     */
    public static final String BANNED_USER = "That user is banned!";

}
