package com.loandemo.Loan.API.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for handling user login requests.
 *
 * <p>This DTO captures the credentials required for user authentication,
 * including username and password.</p>
 *
 * <p>Typically used in authentication APIs where the user provides
 * credentials to receive a JWT token upon successful login.</p>
 *
 * <p>Validation rules:</p>
 * <ul>
 *     <li>Username must not be blank</li>
 *     <li>Password must not be blank</li>
 * </ul>
 *
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Login request body")
public class LoginRequestDto {

    /**
     * Username of the user attempting to log in.
     */
    @Schema(description = "Username", example = "pankaj")
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Password of the user.
     *
     * <p>This will be validated against the stored encrypted password.</p>
     */
    @Schema(description = "password", example = "pan@123")
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Returns a string representation of the login request.
     *
     * <p><b>Warning:</b> Including password in {@code toString()} is not recommended
     * for security reasons in production environments.</p>
     *
     * @return string representation of login request
     */
    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "username='" + username + '\'' +
                '}';
    }
}
