package com.loandemo.Loan.API.dto.transaction;

import com.loandemo.Loan.API.status.EMIStatus;
import com.loandemo.Loan.API.status.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewTransaction {
    private int emiId;
    private String transactionId;
    private double amountPaid;
    private double principleAmount;
    private EMIStatus status;
    private PaymentMode paymentMode;
    private LocalDate paymentData;
}
