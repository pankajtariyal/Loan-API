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

/**
 * Data Transfer Object (DTO) representing detailed information
 * of a specific loan fetched by its unique identifier.
 *
 * <p>This DTO provides comprehensive loan details including
 * user information, loan configuration, approval/rejection status,
 * and timestamps associated with the loan lifecycle.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>Fetching loan details by ID</li>
 *     <li>Loan detail view screens (user/admin)</li>
 *     <li>Loan tracking systems</li>
 * </ul>
 *
 * <p>This DTO combines both user and loan-related data,
 * making it suitable for detailed API responses.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan modal for specific loan id")
public class GetLoanByIdResponse {

    /**
     * Unique identifier of the loan.
     */
    @Schema(description = "Loan id", example = "3")
    private Long id;

    /**
     * Details of the user who applied for the loan.
     */
    @Schema(description = "User detail that apply for loan")
    private UserResponse user;

    /**
     * Total loan amount.
     */
    @Schema(description = "Loan amount", example = "50000")
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
     * PAN number of the user who applied for the loan.
     */
    @Schema(description = "Loaned user panNumber", example = "EWIBA1234A")
    private String panNumber;

    /**
     * Current status of the loan.
     */
    @Schema(description = "Loan status", example = "DOCUMENT_PENDING")
    private LoanStatus status;

    /**
     * Reason for rejection of the loan, if applicable.
     *
     * <p>This field is populated only when the loan status is REJECTED.</p>
     */
    @Schema(description = "Loan rejection reason, If loan get rejected by admin", example = "Document is not clear")
    private String rejected_reason;

    /**
     * User (typically admin) who approved the loan.
     */
    @Schema(description = "Loan approved by", example = "ADMIN")
    private User approvedBy;

    /**
     * Timestamp when the loan was approved.
     */
    @Schema(description = "Loan approved at (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime approvedAt;

    /**
     * Timestamp when the loan was applied/created.
     */
    @Schema(description = "Loan apply at (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;
}
