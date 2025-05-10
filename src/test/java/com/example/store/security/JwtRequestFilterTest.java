package com.example.store.security;

import com.example.store.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JwtRequestFilter}.
 * As this is POC, only success scenario is covered here.
 */
@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private MyUserDetailsService userDetailsService = mock(MyUserDetailsService.class);
    @Mock
    private JwtUtil jwtUtil = mock(JwtUtil.class);
    @Mock
    private HttpServletRequest request = mock(HttpServletRequest.class);
    @Mock
    private HttpServletResponse response = mock(HttpServletResponse.class);
    @Mock
    private FilterChain filterChain = mock(FilterChain.class);
    @Mock
    private UserDetails userDetails = mock(UserDetails.class);

    private JwtRequestFilter target;

    @BeforeEach
    void setUp() {
        target = new JwtRequestFilter(userDetailsService, jwtUtil);
    }

    @Test
    void doFilterInternal_withValidJwt_shouldSucceed() throws Exception {
        // given
        final String token = "valid.jwt.token";
        final String username = "john_doe";
        final String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(List.of());

        // when
        target.doFilterInternal(request, response, filterChain);

        // then
        verify(request).getHeader("Authorization");
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtUtil).validateToken(token, userDetails);
        verify(userDetails).getAuthorities();
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(jwtUtil);
        verifyNoMoreInteractions(userDetailsService);
        verifyNoMoreInteractions(userDetails);
        verifyNoMoreInteractions(filterChain);

        // clear context to avoid side effects
        SecurityContextHolder.clearContext();
    }
}