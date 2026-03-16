package com.loandemo.Loan.API.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration respose modal")
public class UserRegistrationResponse {
    @Schema(description = "Username",example = "pankaj")
    private String username;
    @Schema(description = "Authorization token",example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String jwtToken;
}
