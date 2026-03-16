package com.loandemo.Loan.API.dto.loan.apply;

import com.loandemo.Loan.API.service.LoanService;
import com.loandemo.Loan.API.status.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan Response Modal")
public class LoanApplyResponse {

    @Schema(description = "Loan ID",example = "3")
    private Long loan_id;

    @Schema(description = "Loan amount apply for",example = "50000")
    private double amount;

    @Schema(description = "Loan interest rate",example = "12.5")
    private double interestRate;

    @Schema(description = "Loan tenure in months",example = "12")
    private int tenureMonths;

    @Schema(description = "Loan status",example = "DOCUMENT_PENDING")
    private LoanStatus status;

    @Schema(description = "Loan apply at in date and time",example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;
}
