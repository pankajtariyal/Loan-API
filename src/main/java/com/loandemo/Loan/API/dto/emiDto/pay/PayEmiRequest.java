package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.PaymentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object representing a request to pay an EMI.
 *
 * <p>This DTO is used in API requests when a user wants to make a payment
 * for a specific EMI of a loan.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Payment Request Modal")
public class PayEmiRequest {

    /**
     * The EMI amount that the user wants to pay.
     */
    @Schema(description = "EMI amount to be paid", example = "4050.34")
    @NotNull(message = "Amount must not be empty")
    private double amount;

    /**
     * Payment mode used for the EMI payment.
     *
     * <p>Possible values include:
     * <ul>
     *     <li>UPI</li>
     *     <li>NET_BANKING</li>
     *     <li>CREDIT_CARD</li>
     *     <li>DEBIT_CARD</li>
     * </ul>
     * </p>
     */
    @Schema(description = "EMI Payment Mode", example = "UPI")
    @NotBlank(message = "Please select a payment mode")
    private String paymentMode;
}
