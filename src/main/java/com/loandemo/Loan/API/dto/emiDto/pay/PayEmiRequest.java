package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.PaymentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Pay Modal Request")
public class PayEmiRequest {
    @Schema(description = "EMI amount to be paid",example = "4050.34")
    @NotNull(message = "Amount must not empty")
    private double amount;
    @Schema(description = "EMI Payment Mode",example = "UPI")
    @NotBlank(message = "Please select payment mode")
    private String paymentMode;
}
