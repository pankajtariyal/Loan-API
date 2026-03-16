package com.loandemo.Loan.API.dto.document;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import com.loandemo.Loan.API.status.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Loan document response modal")
public class LoanWithDocument {
    @Schema(description = "Loan Id", example = "3")
    private Long loan_id;
    @Schema(description = "Loan amount", example = "50000")
    private double amount;
    @Schema(description = "Loan interest rate", example = "12.5")
    private double interestRate;
    @Schema(description = "Loan tenure in months", example = "12")
    private int tenureMonths;
    @Schema(description = "Loan status",example = "UNDER_REVIEW")
    private LoanStatus status;
    @Schema(description = "Loan apply at (in date and time)",example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;
    @Schema(description = "Document List")
    private List<DocumentDto> documentList;
}
