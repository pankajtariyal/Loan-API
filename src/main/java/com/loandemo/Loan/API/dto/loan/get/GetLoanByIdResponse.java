package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.status.LoanStatus;
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
public class GetLoanByIdResponse {
    private Long id;
    private UserResponse user;
    private double amount;
    private double interestRate;
    private int tenureMonths;
    private String panNumber;
    private LoanStatus status;
    private String rejected_reason;
    private User approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}
