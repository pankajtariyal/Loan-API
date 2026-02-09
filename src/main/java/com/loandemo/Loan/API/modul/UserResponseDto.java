package com.loandemo.Loan.API.modul;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String role;
    private boolean isActive;
    private String password;
    private String jwtToken;
}
