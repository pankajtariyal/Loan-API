package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.EMIStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayEmiResponse {
    private long loanId;
    private long emiId;
    private String transactionUUID;
    private double amount;
    private EMIStatus status;
}
