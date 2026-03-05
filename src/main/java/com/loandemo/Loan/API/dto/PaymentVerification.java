package com.loandemo.Loan.API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerification {
    private String order_id;
    private String payment_id;
    private String signature;
    private double amount;


}
