package com.loandemo.Loan.API.dto.transaction;

import com.loandemo.Loan.API.status.EMIStatus;
import com.loandemo.Loan.API.status.PaymentMode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing EMI payment transaction details.
 *
 * <p>This DTO is used to provide a summarized view of EMI payment history
 * for a loan. It combines data from {@link com.loandemo.Loan.API.modul.LoanPayment}
 * and {@link com.loandemo.Loan.API.modul.EMISchedule}.</p>
 *
 * <p>Typically used in:</p>
 * <ul>
 *     <li>Viewing payment history of a loan</li>
 *     <li>Transaction reports</li>
 *     <li>User dashboards</li>
 * </ul>
 *
 * <p>Includes both payment details and EMI-related information.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Transaction modal")
public class ViewTransaction {

    /**
     * EMI number or identifier.
     */
    @Schema(description = "EMI id", example = "1")
    private int emiId;

    /**
     * Unique transaction ID for the payment.
     */
    @Schema(description = "EMI Payment transaction id", example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String transactionId;

    /**
     * Amount paid by the user for the EMI.
     */
    @Schema(description = "Emi Amount paid", example = "4050.34")
    private double amountPaid;

    /**
     * Principal portion of the EMI amount.
     */
    @Schema(description = "Emi principle amount to be paid", example = "4050.34")
    private double principleAmount;

    /**
     * Current status of the EMI.
     *
     * <p>Possible values:</p>
     * <ul>
     *     <li>{@link EMIStatus#PAID}</li>
     *     <li>{@link EMIStatus#PENDING}</li>
     *     <li>{@link EMIStatus#DUE}</li>
     * </ul>
     */
    @Schema(description = "EMI Status", example = "PAID/PENDING")
    private EMIStatus status;

    /**
     * Mode of payment used for the transaction.
     *
     * <p>Examples: UPI, CARD, NET_BANKING</p>
     */
    @Schema(description = "Payment mode", example = "UPI")
    private PaymentMode paymentMode;

    /**
     * Date on which the payment was made.
     */
    @Schema(description = "Payment done at (in date)", example = "21-11-2025")
    private LocalDate paymentData;
}
