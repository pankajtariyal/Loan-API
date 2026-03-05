package com.loandemo.Loan.API.dto.loan.apply;

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
public class LoanApplyRequest {
    @NotNull(message = "Loan Amount is required")
     private double amount;
    @NotNull(message = "Interest Rate is required")
     private double interestRate;
    @NotNull(message = "Loan tenure is required")
     private int tenureMonths;
    @NotBlank(message = "Pan number is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
     private String panNumber;
}
