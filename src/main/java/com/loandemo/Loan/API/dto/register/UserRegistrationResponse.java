package com.loandemo.Loan.API.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the response
 * returned after successful user registration.
 *
 * <p>This DTO contains basic user information along with
 * an authentication token (JWT) that can be used for
 * subsequent authorized API requests.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>User registration APIs</li>
 *     <li>Auto-login after successful registration</li>
 * </ul>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration response modal")
public class UserRegistrationResponse {

    /**
     * Username of the registered user.
     */
    @Schema(description = "Username", example = "pankaj")
    private String username;

    /**
     * JWT (JSON Web Token) generated after successful registration.
     *
     * <p>This token is used for authentication and must be sent
     * in the Authorization header for secured API calls.</p>
     */
    @Schema(description = "Authorization token", example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String jwtToken;
}

