package com.loandemo.Loan.API.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing user details
 * returned in API responses.
 *
 * <p>This DTO is used to expose user information safely
 * without including sensitive data such as passwords.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>User profile APIs</li>
 *     <li>Admin user listing</li>
 *     <li>Authentication responses</li>
 * </ul>
 *
 * <p>Includes basic user details along with audit information
 * such as creation and update timestamps.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User detail Modal")
public class UserResponse {

    /**
     * Unique identifier of the user.
     */
    @Schema(description = "User id", example = "2")
    private Long id;

    /**
     * Username of the user.
     */
    @Schema(description = "Username", example = "pankaj")
    private String username;

    /**
     * Email address of the user.
     */
    @Schema(description = "User email", example = "pankaj@gmail.com")
    private String email;

    /**
     * Role assigned to the user (e.g., USER, ADMIN).
     */
    @Schema(description = "User role", example = "USER")
    private String role;

    /**
     * Indicates whether the user account is active.
     */
    @Schema(description = "is User active", example = "true")
    private boolean active;

    /**
     * Timestamp when the user was created.
     */
    @Schema(description = "User register at (in date and time)", example = "2026-02-25 22:23:57.021")
    private String created_at;

    /**
     * Identifier of who created the user.
     */
    @Schema(description = "User created by", example = "pankaj")
    private String created_by;

    /**
     * Timestamp when the user was last updated.
     */
    @Schema(description = "User updated at (in date and time)", example = "2026-02-25 22:23:57.021")
    private String updated_at;

    /**
     * Identifier of who last updated the user.
     */
    @Schema(description = "User updated by", example = "pankaj")
    private String updated_by;
}
