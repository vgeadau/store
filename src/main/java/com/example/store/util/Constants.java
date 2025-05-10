package com.example.store.util;

/**
 * Class used to keep Store specific constants.
 */
public final class Constants {
    /**
     * Constants related to {@link com.example.store.config.GlobalExceptionHandler}.
     */
    public final static String ERROR = "error";

    public final static String AUTHENTICATE = "/authenticate";
    public final static String STORE_EXT = "/store/**";
    public final static String STORE = "/store";

    /**
     * private constructor specific to utility classes.
     */
    private Constants() {
        throw new UnsupportedOperationException("You are not allowed to extend or instantiate this utility class");
    }
}
