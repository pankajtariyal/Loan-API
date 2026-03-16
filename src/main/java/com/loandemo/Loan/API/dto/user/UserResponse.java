package com.loandemo.Loan.API.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User detail Modal")
public class UserResponse {
    @Schema(description = "User id",example = "2")
    private Long id;
    @Schema(description = "Username", example = "pankaj")
    private String username;
    @Schema(description = "User email", example = "pankaj@gmail.com")
    private String email;
    @Schema(description = "User role", example = "USER")
    private String role;
    @Schema(description = "is User active", example = "true")
    private boolean active;
    @Schema(description = "User register at (in date and time)", example = "2026-02-25 22:23:57.021")
    private String created_at;
    @Schema(description = "User created by", example = "pankaj")
    private String created_by;
    @Schema(description = "User updated at (in date and time)", example = "2026-02-25 22:23:57.021")
    private String updated_at;
    @Schema(description = "User updated by ", example = "pankaj")
    private String updated_by;
}
