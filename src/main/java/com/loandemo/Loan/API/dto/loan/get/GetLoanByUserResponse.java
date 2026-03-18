package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Data Transfer Object representing a user along with all loans they have applied for.
 *
 * <p>This DTO is used when retrieving all loans for a specific user,
 * containing both the user details and a list of loan summaries.</p>
 *
 * @see LoanApplyResponse
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NotNull
@Schema(description = "User model containing all loans of the user")
public class GetLoanByUserResponse {

    /**
     * Username of the user.
     */
    @Schema(description = "Username", example = "pankaj")
    private String username;

    /**
     * Email of the user.
     */
    @Schema(description = "User email", example = "pankaj@gmail.com")
    private String email;

    /**
     * List of all loans applied by the user.
     *
     * <p>Each loan in the list contains:
     * <ul>
     *     <li>Loan ID</li>
     *     <li>Amount</li>
     *     <li>Interest rate</li>
     *     <li>Tenure in months</li>
     *     <li>Status</li>
     *     <li>Created at timestamp</li>
     * </ul>
     * </p>
     */
    @Schema(description = "List of user loans with details such as id, amount, interest rate, tenure, status, created at",
            example = "[1, 50000, 12.5, 12, DOCUMENT_PENDING, 2026-02-25 22:23:57.021]")
    private List<LoanApplyResponse> loanList;
}
