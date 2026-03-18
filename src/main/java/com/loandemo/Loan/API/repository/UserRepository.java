package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>This repository provides CRUD operations and additional query methods
 * for user-related operations such as authentication, validation,
 * and uniqueness checks.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Fetch user by username (used for authentication)</li>
 *     <li>Check if username already exists</li>
 *     <li>Check if email already exists</li>
 * </ul>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a user by their username.
     *
     * <p>Commonly used during login/authentication process.</p>
     *
     * @param username the username of the user
     * @return {@link Optional} containing {@link User} if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user exists with the given username.
     *
     * <p>Used for validating unique usernames during registration.</p>
     *
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    boolean existsUserByUsername(String username);

    /**
     * Checks whether a user exists with the given email.
     *
     * <p>Used for validating unique email addresses during registration.</p>
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsUserByEmail(String email);
}
