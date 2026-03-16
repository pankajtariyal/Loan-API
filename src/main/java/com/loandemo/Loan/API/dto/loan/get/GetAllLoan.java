package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.modul.User;
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
@Schema(description = "Loan detail Modal")
public class GetAllLoan {
    @Schema(description = "Loan id", example = "3")
    private Long id;
    @Schema(description = "Loan Amount", example = "50000")
    private double amount;
    @Schema(description = "Loan interest rate", example = "12.5")
    private double interestRate;
    @Schema(description = "Loan tenure in months", example = "12")
    private int tenureMonths;
    @Schema(description = "Loaned user pan number", example = "EWIBA1234A")
    private String panNumber;
    @Schema(description = "Loan status", example = "DOCUMENT_PENDING")
    private LoanStatus status;
    @Schema(description = "Loan rejection reason", example = "Document unclear")
    private String rejected_reason;
    @Schema(description = "Loan approved by", example = "ADMIN")
    private User approvedBy;

    @Schema(description = "Loan approved at(in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime approvedAt;

    @Schema(description = "Loan apply at(in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "GetAllLoan{" +
                "id=" + id +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", tenureMonths=" + tenureMonths +
                ", panNumber='" + panNumber + '\'' +
                ", status=" + status +
                ", rejected_reason='" + rejected_reason + '\'' +
                ", approvedBy=" + approvedBy +
                ", approvedAt=" + approvedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
