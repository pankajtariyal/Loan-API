package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.PaymentMode;
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
public class PayEmiRequest {
    @NotNull(message = "Amount must not empty")
    private double amount;
    @NotBlank(message = "Please select payment mode")
    private String paymentMode;
}
