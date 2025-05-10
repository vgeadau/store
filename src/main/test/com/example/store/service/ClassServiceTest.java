package com.example.store.service;

import com.example.store.dto.AuthRequest;
import com.example.store.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ClassService}.
 */
@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    private final ClassService target = new ClassService();

    @Test
    void create_withDefaultConstructor_shouldSucceed() {
        // given

        // when
        final UserDTO result = target.create(UserDTO.class);

        // then
        assertNotNull(result);
    }

    @Test
    void create_withParameterConstructor_shouldSucceed() {
        // given
        final Class<?>[] types = {String.class, String.class};
        final Object[] values = {USERNAME, PASSWORD};

        // when
        final AuthRequest result = target.create(AuthRequest.class, types, values);

        // then the instance was created with the proper constructor.
        assertTrue(PASSWORD.equals(result.password()) && USERNAME.equals(result.username()));
    }
}
