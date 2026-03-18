package com.loandemo.Loan.API.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the response
 * returned after successful user authentication.
 *
 * <p>This DTO contains basic user information along with
 * a JWT (JSON Web Token) used for accessing secured APIs.</p>
 *
 * <p>The token must be included in the Authorization header
 * (typically as "Bearer &lt;token&gt;") for subsequent requests.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>Login APIs</li>
 *     <li>Authentication workflows</li>
 * </ul>
 *
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Login response modal", description = "Login response if login successful")
public class LoginResponse {

    /**
     * Username of the authenticated user.
     */
    @Schema(name = "username", example = "pankaj")
    private String username;

    /**
     * JWT token generated after successful authentication.
     *
     * <p>This token is used to authorize further API requests.</p>
     */
    @Schema(name = "Authentication token", example = "nkcmoi9o329mio3 20xe23joepk")
    private String token;
}
