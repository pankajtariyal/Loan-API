package com.loandemo.Loan.API.modul;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity representing an email token used for verification or actions
 * such as account activation, password reset, or email validation.
 *
 * <p>Each token is uniquely generated and associated with a specific {@link User}.
 * The token can be marked as used to prevent reuse.</p>
 *
 * <p>This entity helps in implementing secure, one-time operations
 * through token-based validation mechanisms.</p>
 *
 * @since 1.0
 */
@Entity
@Data
@Builder
@Table(name = "email")
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    /**
     * Unique identifier for the email record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique token used for verification purposes.
     *
     * <p>This token is typically sent via email to the user and
     * used for operations like account activation or password reset.</p>
     */
    @Column(name = "token", length = 255, unique = true, nullable = false)
    private String token;

    /**
     * User associated with this email token.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Indicates whether the token has already been used.
     *
     * <p>Once marked as true, the token should not be reused.</p>
     */
    private boolean used;
}
