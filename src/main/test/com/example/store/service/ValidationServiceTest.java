package com.example.store.service;

import com.example.store.exception.StoreException;
import org.junit.jupiter.api.Test;

import static com.example.store.util.ErrorMessages.BANNED_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link ValidationService}.
 */
public class ValidationServiceTest {

    private final ValidationService target = new ValidationService();

    @Test
    public void performAuthenticateValidations_withBannedUser_shouldFail() {
        // given
        final String username = ValidationService.BANNED_LIST.get(0);

        // when
        final StoreException exception = assertThrows(StoreException.class,
                () -> target.performAuthenticateValidations(username));

        // then
        assertEquals(BANNED_USER, exception.getMessage());
    }

    @Test
    public void performAuthenticateValidations_shouldSucceed() {
        // given
        final String username = "some user different from Massador Diego";

        // when
        target.performAuthenticateValidations(username);
    }
}
