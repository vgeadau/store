package com.example.store.service;

import com.example.store.dto.AuthRequest;
import com.example.store.model.MyUserDetails;
import com.example.store.model.User;
import com.example.store.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthService}.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    private final static String TOKEN = "some generated token";

    @Mock
    private final MyUserDetailsService userDetailsService = mock(MyUserDetailsService.class);
    @Mock
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    @Mock
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    @Mock
    private final ValidationService validationService = mock(ValidationService.class);
    @Mock
    private final ClassService classService = mock(ClassService.class);

    private AuthService target;

    @BeforeEach
    void setUp() {
        target = new AuthService(userDetailsService, jwtUtil, authenticationManager,
                validationService, classService);
    }

    @Test
    void authenticate_shouldSucceed() {
        // given
        final AuthRequest authRequest = new AuthRequest(USERNAME, PASSWORD);

        final Class<?>[] types = {Object.class, Object.class};
        final Object[] values = {authRequest.username(), authRequest.password()};
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.username(), authRequest.password());
        final UserDetails userDetails = getMyUserDetails();

        // by using this instance service we can obtain an instance of UsernamePasswordAuthenticationToken
        // without ugly hacks such as any(), argument capture or spy.
        when(classService.create(UsernamePasswordAuthenticationToken.class, types, values)).thenReturn(authenticationToken);
        when(userDetailsService.loadUserByUsername(authRequest.username())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(TOKEN);

        // when
        final String result = target.authenticate(authRequest);

        // then
        verify(validationService).performAuthenticateValidations(authRequest.username());
        verify(classService).create(UsernamePasswordAuthenticationToken.class, types, values);
        verify(authenticationManager).authenticate(authenticationToken);
        verify(userDetailsService).loadUserByUsername(authRequest.username());
        verify(jwtUtil).generateToken(userDetails);
        verifyNoMoreInteractions(validationService, classService, authenticationManager, userDetailsService, jwtUtil);

        assertEquals(TOKEN, result);
    }

    /**
     * create an object used for testing purposes.
     * @return MyUserDetails
     */
    private MyUserDetails getMyUserDetails() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        return new MyUserDetails(user);
    }
}
