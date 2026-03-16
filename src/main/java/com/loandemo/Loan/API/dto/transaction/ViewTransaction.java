package com.loandemo.Loan.API.dto.transaction;

import com.loandemo.Loan.API.status.EMIStatus;
import com.loandemo.Loan.API.status.PaymentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Transaction modal")
public class ViewTransaction {
    @Schema(description = "EMI id", example = "1")
    private int emiId;
    @Schema(description = "EMI Payment transaction id", example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String transactionId;
    @Schema(description = "Emi Amount paid", example = "4050.34")
    private double amountPaid;
    @Schema(description = "Emi principle amount to be paid", example = "4050.34")
    private double principleAmount;
    @Schema(description = "EMI Status",example = "PAID/PENDING")
    private EMIStatus status;
    @Schema(description = "Payment mode", example = "UPI")
    private PaymentMode paymentMode;
    @Schema(description = "Payment done at (in date)", example = "21-11-2025")
    private LocalDate paymentData;
}
