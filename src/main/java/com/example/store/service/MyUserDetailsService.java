package com.example.store.service;

import com.example.store.exception.StoreException;
import com.example.store.model.MyUserDetails;
import com.example.store.model.User;
import com.example.store.repository.UserRepository;
import com.example.store.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Service handling the user related operations.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates an MyUserDetails object based on the User found on the repository using username
     * @param username String
     * @return UserDetails object
     * @throws UsernameNotFoundException on errors
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.NOT_FOUND + username));
        return user.map(MyUserDetails::new).get();
    }

    /**
     * Creates a user.
     * If incorrect user provided or, a username / pseudonym that already exists,
     * we will throw a generic store exception.
     * @param user User
     * @return User
     */
    public User save(User user) {
        if (Objects.isNull(user.getPseudonym()) || Objects.isNull(user.getUsername())
                || Objects.isNull(user.getPassword())) {
            throw new StoreException(ErrorMessages.INVALID_USER);
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new StoreException(ErrorMessages.INVALID_USER);
        }
        if (userRepository.findByPseudonym(user.getPseudonym()).isPresent()) {
            throw new StoreException(ErrorMessages.INVALID_USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * gets current logged user.
     * @return String
     */
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}