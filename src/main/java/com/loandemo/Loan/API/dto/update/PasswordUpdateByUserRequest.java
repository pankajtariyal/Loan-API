package com.loandemo.Loan.API.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for handling user password update requests.
 *
 * <p>This DTO is used when a user wants to change their password.
 * It contains the current password for verification and the new password
 * to be updated.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>All fields are mandatory</li>
 *     <li>Current password must match the existing password</li>
 * </ul>
 *
 * <p>Typically used in secured APIs where the user is authenticated.</p>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Password Update request body")
public class PasswordUpdateByUserRequest {

    /**
     * Username of the user requesting password change.
     *
     * <p>Used to identify the account for which the password
     * needs to be updated.</p>
     */
    @Schema(description = "Username", example = "pankaj")
    @NotBlank(message = "username is required")
    private String username;

    /**
     * Current password of the user.
     *
     * <p>This is validated against the stored password before allowing update.</p>
     */
    @NotBlank(message = "password is required")
    @Schema(description = "User current password", example = "Pankaj@123")
    private String password;

    /**
     * New password to be set for the user.
     *
     * <p>This will replace the existing password after successful validation.</p>
     */
    @NotBlank(message = "new password is required")
    @Schema(description = "User new password", example = "Pankaj1")
    private String new_password;
}

