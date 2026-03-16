package com.loandemo.Loan.API.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Password Update request body")
public class PasswordUpdateByUserRequest {
    @Schema(description = "Username",example = "pankaj")
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
    @Schema(description = "User current password",example = "Pankaj@123")
    private String password;
    @NotBlank(message = "new password is required")
    @Schema(description = "User new password",example = "Pankaj1")
    private String new_password;
}
