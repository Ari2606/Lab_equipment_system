package com.example.labtrack.repository;

import com.example.labtrack.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository provides database access operations for the User entity.
 *
 * Extends JpaRepository to inherit standard CRUD operations without
 * requiring manual SQL or boilerplate DAO code.
 *
 * Custom query methods are defined here using Spring Data JPA's method
 * naming convention, which automatically generates the appropriate SQL.
 *
 * This repository is used in two main contexts:
 *  1. Authentication  : Spring Security calls findByUsername() via CustomUserDetailsService
 *  2. Business logic  : UserServiceImpl calls the existence checks and lookup methods
 *                       during registration and user management operations
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     * Used by CustomUserDetailsService to load the user during login.
     *
     * Generated query: SELECT * FROM users WHERE username = :username
     *
     * @param username the login username to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     * Can be used for password reset or account lookup flows.
     *
     * Generated query: SELECT * FROM users WHERE email = :email
     *
     * @param email the email address to search for
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks whether a user with the given username already exists.
     * Called during registration to prevent duplicate usernames.
     *
     * Generated query: SELECT COUNT(*) > 0 FROM users WHERE username = :username
     *
     * @param username the username to check
     * @return true if a user with this username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether a user with the given email address already exists.
     * Called during registration to prevent duplicate email addresses.
     *
     * Generated query: SELECT COUNT(*) > 0 FROM users WHERE email = :email
     *
     * @param email the email address to check
     * @return true if a user with this email exists, false otherwise
     */
    boolean existsByEmail(String email);
}