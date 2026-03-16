package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NotNull
@Schema(description = "User modal containing user all loan")
public class GetLoanByUserResponse {
    @Schema(description = "Username",example = "pankaj")
    private String username;
    @Schema(description = "User email", example = "pankaj@gamil.com")
    private String email;
    @Schema(description = "User all loan list with id, amount, interest rate, tenure, status, created at.", example = "[1, 50000, 12.5, 12, DOCUMENT_PENDING,2026-02-25 22:23:57.021]")
    private List<LoanApplyResponse> loanList;
}
