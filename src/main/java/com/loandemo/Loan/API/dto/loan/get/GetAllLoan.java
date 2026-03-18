package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.status.LoanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing detailed loan information.
 *
 * <p>This DTO is used to fetch and expose complete loan details,
 * typically for admin or reporting purposes. It includes financial,
 * user-related, and status-related information of a loan.</p>
 *
 * <p>It is commonly used in:</p>
 * <ul>
 *     <li>Admin dashboards</li>
 *     <li>Loan listing APIs</li>
 *     <li>Loan audit/reporting systems</li>
 * </ul>
 *
 * <p>The DTO contains both approval and rejection details,
 * making it suitable for tracking the full lifecycle of a loan.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan detail Modal")
public class GetAllLoan {

    /**
     * Unique identifier of the loan.
     */
    @Schema(description = "Loan id", example = "3")
    private Long id;

    /**
     * Total loan amount.
     */
    @Schema(description = "Loan Amount", example = "50000")
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
    @Schema(description = "Loaned user pan number", example = "EWIBA1234A")
    private String panNumber;

    /**
     * Current status of the loan.
     */
    @Schema(description = "Loan status", example = "DOCUMENT_PENDING")
    private LoanStatus status;

    /**
     * Reason for loan rejection (if applicable).
     *
     * <p>This field is populated only when the loan status is REJECTED.</p>
     */
    @Schema(description = "Loan rejection reason", example = "Document unclear")
    private String rejected_reason;

    /**
     * User (typically admin) who approved the loan.
     */
//    @Schema(description = "Loan approved by", example = "ADMIN")
//    private UserResponse approvedBy;

    /**
     * Timestamp when the loan was approved.
     */
    @Schema(description = "Loan approved at(in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime approvedAt;

    /**
     * Timestamp when the loan was created/applied.
     */
    @Schema(description = "Loan apply at(in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime createdAt;

    /**
     * Returns a string representation of the loan details.
     *
     * @return string representation of loan object
     */
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
//                ", approvedBy=" + approvedBy +
                ", approvedAt=" + approvedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
