package com.loandemo.Loan.API.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Data Transfer Object (DTO) for handling user registration requests.
 *
 * <p>This DTO captures the necessary details required to register a new user
 * in the system, including username, email, and password.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>Username must start with a letter and be 5–17 characters long</li>
 *     <li>Username can contain letters, digits, and underscore</li>
 *     <li>Email must be valid format</li>
 *     <li>Password must not be null</li>
 * </ul>
 *
 * <p>This DTO is typically used in registration APIs to create new user accounts.</p>
 *
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Registration Request Modal")
public class UserRegistrationRequest {

    /**
     * Username for the new user.
     *
     * <p>Constraints:</p>
     * <ul>
     *     <li>Must start with a letter</li>
     *     <li>Length between 5 and 17 characters</li>
     *     <li>Allowed characters: letters, digits, underscore</li>
     * </ul>
     */
    @Schema(description = "username", example = "pankaj")
    @NotBlank(message = "username is required.")
    @Pattern(
            regexp = "^[a-zA-z][a-zA-Z0-9_]{4,16}$",
            message = "Username must start with a letter and length should be between 5 to 17 characters"
    )
    private String username;

    /**
     * Email address of the user.
     *
     * <p>Must follow a valid email format.</p>
     */
    @Schema(description = "User email", example = "pankaj@gmail.com")
    @Email(message = "invalid email.")
    private String email;

    /**
     * Password for the user account.
     *
     * <p>Should be securely stored using encryption (e.g., BCrypt).</p>
     */
    @Schema(description = "User password", example = "Pankaj@123")
    @NotBlank(message = "password is required")
    private String password;

    @Override
    public String toString() {
        return "UserRegistrationRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
