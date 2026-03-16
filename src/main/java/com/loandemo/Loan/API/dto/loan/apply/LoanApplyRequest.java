package com.loandemo.Loan.API.dto.loan.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan Request Model")
public class LoanApplyRequest {

    @Schema(description = "Loan amount", example = "50000")
    @NotNull(message = "Loan Amount is required")
     private double amount;

    @Schema(description = "Interest Rate", example = "12")
    @NotNull(message = "Interest Rate is required")
     private double interestRate;

    @Schema(description = "Loan tenure in months", example = "12")
    @NotNull(message = "Loan tenure is required")
     private int tenureMonths;

    @Schema(description = "Pan Number", example = "EWIPA2398A")
    @NotBlank(message = "Pan number is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
     private String panNumber;
}
