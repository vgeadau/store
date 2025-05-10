package com.example.store.config;

import com.example.store.security.JwtRequestFilter;
import com.example.store.service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SecurityConfig}.
 */
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {
    /**
     * Beans used by the class which is being tested.
     */
    @Mock
    private MyUserDetailsService myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
    @Mock
    private JwtRequestFilter jwtRequestFilter = Mockito.mock(JwtRequestFilter.class);

    /**
     * Class being tested (named as target).
     */
    @InjectMocks
    private SecurityConfig target;

    @Test
    void passwordEncoder_should_succeed() {

        // when
        final PasswordEncoder result = target.passwordEncoder();

        // then
        verifyNoInteractions(myUserDetailsService, jwtRequestFilter);

        assertNotNull(result);
    }

    @Test
    @SuppressWarnings({"unchecked","rawtypes"})
    void configure_withAuthenticationManagerBuilderParam_should_succeed() throws Exception {
        // given
        final AuthenticationManagerBuilder builder = Mockito.mock(AuthenticationManagerBuilder.class);
        final DaoAuthenticationConfigurer config = Mockito.mock(DaoAuthenticationConfigurer.class);
        String error = "NO ERROR";

        when(builder.userDetailsService(myUserDetailsService)).thenReturn(config);

        // when
        try {
            target.configure(builder);
        } catch (Exception e) {
            error = "ERROR";
        }

        // then
        verify(builder).userDetailsService(myUserDetailsService);
        verifyNoInteractions(myUserDetailsService, jwtRequestFilter);

        assertEquals("NO ERROR", error);
    }
}
