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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Registration Request Modal")
public class UserRegistrationRequest {
    @Schema(description = "username", example = "pankaj")
    @NotBlank(message = "username is required.")
    @Pattern(regexp = "^[a-zA-z][a-zA-Z0-9_]{4,16}$",
            message = "Username must start with Letter and character must be between 4 to 16")
    private String username;

    @Schema(description = "User email", example = "pankaj@gmail.com")
    @Email(message = "invalid email.")
    private String email;
    @Schema(description = "User password",example = "Pankaj@123")
    @NotNull(message = "password can be empty")
    private String password;
}
