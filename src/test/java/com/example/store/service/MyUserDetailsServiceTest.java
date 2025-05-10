package com.example.store.service;

import com.example.store.model.User;
import com.example.store.repository.UserRepository;
import com.example.store.util.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link MyUserDetailsService}.
 */
@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    private static final String USER_NAME = "username";
    private static final String INVALID_USER_NAME = "invalid username";
    private static final Long ID = 1L;
    private static final String ENCODED_PASSWORD = "encoded password";

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    @InjectMocks
    private MyUserDetailsService target;

    @Test
    public void loadUserByUsername_shouldSucceed() {
        // given
        final User user = buildUser();

        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));

        // when
        final UserDetails userDetails = target.loadUserByUsername(USER_NAME);

        // then
        verify(userRepository).findByUsername(USER_NAME);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);

        assertEquals(userDetails.getUsername(), user.getUsername());
        assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test
    public void loadUserByUsername_withUserNotFound_shouldFail() {
        // given
        when(userRepository.findByUsername(INVALID_USER_NAME)).thenReturn(Optional.empty());

        // when
        final Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> target.loadUserByUsername(INVALID_USER_NAME));

        // then
        verify(userRepository).findByUsername(INVALID_USER_NAME);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);

        assertEquals(ErrorMessages.NOT_FOUND + INVALID_USER_NAME, exception.getMessage());
    }

    @Test
    public void save_shouldSucceed() {
        // given
        final User user = buildUser();
        final String password = "pass";
        user.setPassword(password);

        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);

        // when
        final User result = target.save(user);

        // then
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(user);

        assertEquals(user, result);
    }

    /**
     * builds an object used for testing.
     * @return User
     */
    private User buildUser() {
        final User user = new User();
        user.setUsername("username");
        user.setPseudonym("pseudonym");

        user.setId(ID);
        return user;
    }

}
