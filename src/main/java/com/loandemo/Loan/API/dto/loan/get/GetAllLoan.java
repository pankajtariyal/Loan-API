package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.status.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllLoan {
    private Long id;
    private double amount;
    private double interestRate;
    private int tenureMonths;
    private String panNumber;
    private LoanStatus status;
    private String rejected_reason;
    private User approvedBy;
    private LocalDateTime approvedAt;
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
