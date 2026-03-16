package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.status.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan modal for specific loan id")
public class GetLoanByIdResponse {
    @Schema(description = "Loan id", example = "3")
    private Long id;
    @Schema(description = "User detail that apply for loan")
    private UserResponse user;
    @Schema(description = "Loan amount", example = "50000")
    private double amount;
    @Schema(description = "Loan interest rate", example = "12.5")
    private double interestRate;
    @Schema(description = "Loan tenure in months", example = "12")
    private int tenureMonths;
    @Schema(description = "Loaned user panNumber", example = "EWIBA1234A")
    private String panNumber;
    @Schema(description = "Loan status", example = "DOCUMENT_PENDING")
    private LoanStatus status;
    @Schema(description = "Loan rejection reason, If loan get rejected by admin ", example = "Document is not clear")
    private String rejected_reason;
    @Schema(description = "Loan approved by", example = "ADMIN")
    private User approvedBy;
    @Schema(description = "Loan approved at (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime approvedAt;
    @Schema(description = "Loan apply at (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;
}
