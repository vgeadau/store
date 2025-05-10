package com.example.store.controller;

import com.example.store.dto.AuthRequest;
import com.example.store.model.User;
import com.example.store.service.AuthService;
import com.example.store.service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuthController}.
 * </br>
 * Note: "verifies" gives us certainty that if we somehow change the code in order to test faster a functionality,
 * we don't forget to re-add the code back.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String TOKEN = "some token is here...";

    @Mock
    private AuthService authService = Mockito.mock(AuthService.class);

    @Mock
    private MyUserDetailsService userDetailsService = Mockito.mock(MyUserDetailsService.class);

    @InjectMocks
    private AuthController target;

    @Test
    void authenticate_shouldSucceed() {
        // given
        final AuthRequest authRequest = buildAuthRequest();

        when(authService.authenticate(authRequest)).thenReturn(TOKEN);

        // when
        final String result = target.authenticate(authRequest);

        // then
        verify(authService).authenticate(authRequest);
        verifyNoMoreInteractions(authService);
        verifyNoInteractions(userDetailsService);

        assertEquals(TOKEN, result);
    }

    @Test
    void register_shouldSucceed() {
        // given
        final User user = buildUser();

        when(userDetailsService.save(user)).thenReturn(user);

        // when
        final User result = target.register(user);

        // then
        verify(userDetailsService).save(user);
        verifyNoMoreInteractions(userDetailsService);
        verifyNoInteractions(authService);

        assertEquals(user, result);
    }

    /**
     * builds an object used for testing.
     * @return AuthRequest
     */
    private AuthRequest buildAuthRequest() {
        return new AuthRequest("user", "pass");
    }

    /**
     * builds an object used for testing.
     * @return User
     */
    private User buildUser() {
        final User user = new User();
        user.setPassword("pass");
        user.setPseudonym("pseudonym");
        user.setUsername("user");
        return user;
    }

}
