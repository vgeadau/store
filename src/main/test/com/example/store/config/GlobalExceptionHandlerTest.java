package com.example.store.config;

import com.example.store.exception.StoreException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.store.util.Constants.*;
import static com.example.store.util.ErrorMessages.*;

import java.util.Map;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * A memory improvement can be importing only needed static elements,
 * but to avoid cluttering with more lines of code, we import all.
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    /**
     * Constants specific to current unit test.
     */
    private final static String SOME_STORE_ERROR_MESSAGE = "some store exception message";
    private final static String SOME_GENERIC_ERROR_MESSAGE = "some generic exception message";

    /**
     * Class being tested (named as target).
     */
    private final GlobalExceptionHandler target = new GlobalExceptionHandler();

    @Test
    void handleStoreException_shouldSucceed() {
        final StoreException ex = new StoreException(SOME_STORE_ERROR_MESSAGE);

        // when
        final ResponseEntity<Map<String, String>> result = target.handleStoreException(ex);

        // then (ideally we have a single assert, but in this particular case we need 4).
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().containsKey(ERROR));
        assertEquals(result.getBody().get(ERROR), SOME_STORE_ERROR_MESSAGE);
    }

    @Test
    void handleGenericException_shouldSucceed() {
        // given
        final Exception ex = new Exception(SOME_GENERIC_ERROR_MESSAGE);

        // when
        final ResponseEntity<Map<String, String>> result = target.handleGenericException(ex);

        // then (ideally we have a single assert, but in this particular case we need 4).
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().containsKey(ERROR));
        assertEquals(result.getBody().get(ERROR), GENERIC_EXCEPTION_RESPONSE_MESSAGE);
    }
}
