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
     * {@link com.example.store.config.GlobalExceptionHandler} error messages.
     */
    public final static String STORE_EXCEPTION_LOG_MESSAGE = "Store exception occurred: ";
    public final static String GENERIC_EXCEPTION_LOG_MESSAGE = "Handled generic exception: ";
    public final static String GENERIC_EXCEPTION_RESPONSE_MESSAGE
            = "An unexpected error occurred. Please, contact support!";

    /**
     * {@link com.example.store.service.ProductService} error messages.
     */
    public static final String PRODUCT_NOT_FOUND = "Product not found!";
    public static final String INVALID_USER = "Invalid user";
    public static final String REMOVE_NOT_ALLOWED = "You are not allowed to remove this product";
    public static final String PRODUCT_HAS_INVALID_USER = "Attempting to persist a product that doesn't have an existing author!";

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
