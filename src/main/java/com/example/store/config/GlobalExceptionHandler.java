package com.example.store.config;

import com.example.store.exception.StoreException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.store.util.ErrorMessages.*;
import static com.example.store.util.Constants.*;

/**
 * Exception handler class.
 * </br>
 * As exceptions are handled in this class, we log exceptions here to avoid cluttering the code with
 * duplicated logs.
 * </br>
 * Externalization of constants (see Constants and ErrorMessages)
 * to avoid having magic values (such as string in below case). The second purpose is that
 * they can be used in the unit tests, and once the text message is updated, the unit test won't be affected
 * as it relies on the name of the constant rather than some value which can be changed in the future.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * handle store exception.
     * @param ex StoreException
     * @return ResponseEntity
     */
    @ExceptionHandler(StoreException.class)
    public ResponseEntity<Map<String, String>> handleStoreException(StoreException ex) {
        logger.error(STORE_EXCEPTION_LOG_MESSAGE, ex);
        final Map<String, String> errorBody = new HashMap<>();
        errorBody.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    /**
     * handle other exceptions.
     * @param ex Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error(GENERIC_EXCEPTION_LOG_MESSAGE, ex);
        final Map<String, String> errorBody = new HashMap<>();
        errorBody.put(ERROR, GENERIC_EXCEPTION_RESPONSE_MESSAGE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}