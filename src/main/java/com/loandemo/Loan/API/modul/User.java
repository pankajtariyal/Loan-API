package com.loandemo.Loan.API.modul;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entity representing a user in the system.
 *
 * <p>This entity stores authentication and profile-related information
 * required for login, authorization, and auditing purposes.</p>
 *
 * <p>Each user has a role which determines access control
 * (e.g., USER, ADMIN).</p>
 *
 * <p>The entity also maintains audit fields such as creation
 * and update timestamps, along with activation and verification status.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "user_master")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * Unique username used for login.
     */
    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    /**
     * Unique email address of the user.
     */
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    /**
     * Encrypted password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Role assigned to the user.
     *
     * <p>Example values: USER, ADMIN</p>
     */
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    /**
     * Indicates whether the user account is active.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * Timestamp when the user was created.
     */
    private Timestamp created_at;

    /**
     * Username or system identifier who created this user.
     */
    @Column(name = "created_by", length = 20)
    private String created_by;

    /**
     * Timestamp when the user was last updated.
     */
    private Timestamp updated_at;

    /**
     * Username or system identifier who last updated this user.
     */
    @Column(name = "updated_by", length = 20)
    private String updated_by;

    /**
     * Indicates whether the user has completed verification
     * (e.g., email verification).
     */
    private boolean verification;

    /**
     * Returns a string representation of the user.
     *
     * <p><b>Note:</b> Including password in {@code toString()} is not recommended
     * for security reasons in production systems.</p>
     *
     * @return string representation of user
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", created_at=" + created_at +
                ", created_by='" + created_by + '\'' +
                ", updated_at=" + updated_at +
                ", updated_by='" + updated_by + '\'' +
                ", verification=" + verification +
                '}';
    }
}
