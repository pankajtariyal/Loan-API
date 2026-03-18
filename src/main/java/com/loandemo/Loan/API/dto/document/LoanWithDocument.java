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

/**
 * Data Transfer Object representing a loan along with its associated documents.
 *
 * <p>This DTO is used to return loan details to the client, including
 * loan amount, interest rate, tenure, status, creation timestamp,
 * and a list of associated documents.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan document response modal")
public class LoanWithDocument {

    /**
     * Unique identifier of the loan.
     */
    @Schema(description = "Loan Id", example = "3")
    private Long loan_id;

    /**
     * Total amount of the loan applied for.
     */
    @Schema(description = "Loan amount", example = "50000")
    private double amount;

    /**
     * Interest rate of the loan.
     */
    @Schema(description = "Loan interest rate", example = "12.5")
    private double interestRate;

    /**
     * Tenure of the loan in months.
     */
    @Schema(description = "Loan tenure in months", example = "12")
    private int tenureMonths;

    /**
     * Current status of the loan.
     *
     * <p>Possible values include {@link LoanStatus#DOCUMENT_PENDING},
     * {@link LoanStatus#UNDER_REVIEW}, {@link LoanStatus#APPROVED},
     * {@link LoanStatus#REJECTED}, etc.</p>
     */
    @Schema(description = "Loan status", example = "UNDER_REVIEW")
    private LoanStatus status;

    /**
     * Timestamp when the loan was applied.
     */
    @Schema(description = "Loan apply at (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;

    /**
     * List of documents associated with this loan.
     *
     * <p>Each document in the list contains details like type, status, rejection reason, and upload timestamp.</p>
     */
    @Schema(description = "Document List")
    private List<DocumentDto> documentList;
}
