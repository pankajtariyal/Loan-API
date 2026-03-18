package com.loandemo.Loan.API.dto.loan.apply;

import com.loandemo.Loan.API.service.LoanService;
import com.loandemo.Loan.API.status.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the response
 * after a loan application is created or fetched.
 *
 * <p>This DTO provides essential loan details such as
 * loan amount, interest rate, tenure, current status,
 * and the timestamp when the loan was applied.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>Loan application response APIs</li>
 *     <li>Fetching user-specific loan details</li>
 * </ul>
 *
 * <p>The loan status indicates the current stage of the loan lifecycle,
 * such as DOCUMENT_PENDING, APPROVED, REJECTED, etc.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan Response Modal")
public class LoanApplyResponse {

    /**
     * Unique identifier of the loan.
     */
    @Schema(description = "Loan ID", example = "3")
    private Long loan_id;

    /**
     * Total loan amount applied by the user.
     */
    @Schema(description = "Loan amount apply for", example = "50000")
    private double amount;

    /**
     * Interest rate applied to the loan.
     *
     * <p>Usually expressed as an annual percentage.</p>
     */
    @Schema(description = "Loan interest rate", example = "12.5")
    private double interestRate;

    /**
     * Loan tenure in months.
     */
    @Schema(description = "Loan tenure in months", example = "12")
    private int tenureMonths;

    /**
     * Current status of the loan.
     */
    @Schema(description = "Loan status", example = "DOCUMENT_PENDING")
    private LoanStatus status;

    /**
     * Timestamp indicating when the loan was created/applied.
     */
    @Schema(description = "Loan apply at in date and time", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;
}
