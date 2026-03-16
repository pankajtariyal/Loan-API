package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.EMIStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Pay response Modal")
public class PayEmiResponse {
    @Schema(description = "Loan id paid emi of",example = "3")
    private long loanId;
    @Schema(description = "EMI(id) to paid of loan", example = "1")
    private long emiId;
    @Schema(description = "EMI Payment transaction", example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String transactionUUID;
    @Schema(description = "Emi principle amount paid", example = "4050.34")
    private double amount;
    @Schema(description = "EMI status",example = "PAID")
    private EMIStatus status;
}
