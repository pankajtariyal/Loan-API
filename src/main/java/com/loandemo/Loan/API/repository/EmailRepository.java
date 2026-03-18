package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Email} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide
 * standard CRUD operations and custom query methods related
 * to email verification tokens.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Store email verification tokens</li>
 *     <li>Retrieve tokens for verification</li>
 *     <li>Support email verification workflow</li>
 * </ul>
 *
 * <p>Used in:</p>
 * <ul>
 *     <li>User registration process</li>
 *     <li>Email verification flow</li>
 * </ul>
 *
 * <p>Verification Flow:</p>
 * <pre>
 * Generate Token →
 * Save Token in DB →
 * Send Email →
 * User clicks verification link →
 * Fetch token using repository →
 * Validate and activate user
 * </pre>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    /**
     * Finds an email verification record by token.
     *
     * <p>This method is used during email verification to
     * validate the token received from the verification link.</p>
     *
     * @param token unique verification token
     * @return {@link Optional} containing {@link Email} if found,
     *         otherwise empty
     */
    Optional<Email> findByToken(String token);
}
