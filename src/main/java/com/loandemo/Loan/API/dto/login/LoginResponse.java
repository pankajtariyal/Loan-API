package com.loandemo.Loan.API.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Login response modal", description = "Login response if login successfull")
public class LoginResponse {
    @Schema(name = "username", example = "pankaj")
    private String username;
    @Schema(name = "Authentication token",example = "nkcmoi9o329mio3 20xe23joepk")
    private String token;
}
