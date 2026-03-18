package com.loandemo.Loan.API.jwttoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 *
 * <p>This class provides methods for generating, parsing, and validating JWT tokens.
 * It uses a secret key to sign tokens and verify their authenticity.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Generate JWT tokens with username and role</li>
 *     <li>Extract claims from a token</li>
 *     <li>Extract username and expiration date</li>
 *     <li>Validate token expiration</li>
 * </ul>
 *
 * <p>Token Structure:</p>
 * <ul>
 *     <li><b>Header:</b> Contains algorithm (HS256)</li>
 *     <li><b>Payload:</b> Contains claims (username, role, issued date, expiration)</li>
 *     <li><b>Signature:</b> Generated using secret key</li>
 * </ul>
 *
 * <p>Example Token Payload:</p>
 * <pre>
 * {
 *   "sub": "username",
 *   "role": "ROLE_USER",
 *   "iat": 1690000000,
 *   "exp": 1690003600
 * }
 * </pre>
 *
 * <p>Note:</p>
 * <ul>
 *     <li>Token validity is set to 1 hour</li>
 *     <li>Uses HMAC SHA-256 algorithm for signing</li>
 * </ul>
 *
 * @author Abhishek
 */
@Component
public class JwtUtils {

    /**
     * Secret key used for signing JWT tokens.
     */
    private final String SECRET_KEY = "kodduonhcdonhbfh32oinwhbe3iwdwdhedlkx";

    /**
     * Generates a secure signing key from the secret string.
     *
     * @return {@link Key} used for signing and validating JWT tokens
     */
    public Key getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for a given username and role.
     *
     * <p>The token includes:</p>
     * <ul>
     *     <li>Subject (username)</li>
     *     <li>Custom claim (role)</li>
     *     <li>Issued time</li>
     *     <li>Expiration time (1 hour)</li>
     * </ul>
     *
     * @param username the username of the authenticated user
     * @param role the role of the user (e.g., ROLE_USER, ROLE_ADMIN)
     * @return generated JWT token as a String
     */
    public String generateToken(String username, String role){
        HashMap<String,Object > claims = new HashMap<>();
        claims.put("role",role);

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return {@link Claims} object containing token data
     */
    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return username stored in the token
     */
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token){
        return extractClaims(token).getExpiration();
    }

    /**
     * Checks whether the JWT token is expired.
     *
     * <p><b>Important:</b> This method currently contains a logical issue.</p>
     *
     * @param token the JWT token
     * @return {@code true} if token is NOT expired, {@code false} otherwise
     *
     * <p>Correct logic should be:</p>
     * <pre>
     * return extractExpiration(token).before(new Date());
     * </pre>
     */
    public boolean isTokenExpired(String token){
        return !extractExpiration(token).before(new Date());
    }
}
