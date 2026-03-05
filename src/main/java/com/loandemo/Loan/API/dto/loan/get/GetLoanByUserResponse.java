package com.loandemo.Loan.API.dto.loan.get;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NotNull
public class GetLoanByUserResponse {
    private String username;
    private String email;
    private List<LoanApplyResponse> loanList;
}
