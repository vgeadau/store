package com.example.store.service;

import com.example.store.dto.AuthRequest;
import com.example.store.exception.StoreException;
import com.example.store.security.JwtUtil;
import com.example.store.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Authentication Service class.
 */
@Service
public class AuthService {
    private final MyUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final ValidationService validationService;

    private final ClassService classService;

    @Autowired
    public AuthService(MyUserDetailsService userDetailsService, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, ValidationService validationService,
                       ClassService classService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.validationService = validationService;
        this.classService = classService;
    }

    /**
     * Authenticate method.
     * By having ClassService which creates an instance the code respects D from SOLID.
     * See more clarifications in the comments of ClassService.
     * @param authRequest the Authentication Request
     * @return JWT string token
     */
    public String authenticate(AuthRequest authRequest) {
        // Bonus implementation. Some users could be banned, so we perform a validation.
        validationService.performAuthenticateValidations(authRequest.username());

        try {
            final Class<?>[] types = {String.class, String.class};
            final Object[] values = {authRequest.username(), authRequest.password()};
            final UsernamePasswordAuthenticationToken authenticationToken =
                    classService.create(UsernamePasswordAuthenticationToken.class, types, values);
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new StoreException(ErrorMessages.AUTHENTICATION_ERROR, e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());

        return jwtUtil.generateToken(userDetails);
    }
}
