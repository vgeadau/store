package com.example.store.repository;

import com.example.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository class.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a User by its username
     * @param username String
     * @return User object
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a User by its pseudonym
     * @param pseudonym String
     * @return User object
     */
    Optional<User> findByPseudonym(String pseudonym);
}