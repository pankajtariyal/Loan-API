package com.loandemo.Loan.API.dto.loan.apply;

import com.loandemo.Loan.API.service.LoanService;
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
public class LoanApplyResponse {
    private Long loan_id;
    private double amount;
    private double interestRate;
    private int tenureMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;
}
