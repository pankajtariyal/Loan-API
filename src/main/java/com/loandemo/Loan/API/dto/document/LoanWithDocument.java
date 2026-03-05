package com.loandemo.Loan.API.dto.document;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import com.loandemo.Loan.API.status.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanWithDocument {
    private Long loan_id;
    private double amount;
    private double interestRate;
    private int tenureMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private List<DocumentDto> documentList;
}
