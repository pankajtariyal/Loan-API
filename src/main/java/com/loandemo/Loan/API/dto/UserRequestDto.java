package com.loandemo.Loan.API.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    @NotBlank(message = "username is required.")
    @Pattern(regexp = "^[a-zA-z][a-zA-Z0-9_]{4,16}$",
    message = "Username must start with Letter and character must be between 4 to 16")
    private String username;
    @Email(message = "invalid email.")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
