package com.loandemo.Loan.API.dto.emiDto.pay;

import com.loandemo.Loan.API.status.EMIStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing the response after making an EMI payment.
 *
 * <p>This DTO is returned to the client after a successful EMI payment and
 * contains details about the payment, including the EMI, loan, amount paid,
 * transaction ID, and the updated status.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI Payment Response Modal")
public class PayEmiResponse {

    /**
     * The ID of the loan for which the EMI was paid.
     */
    @Schema(description = "Loan ID for which EMI is paid", example = "3")
    private long loanId;

    /**
     * The ID of the EMI that was paid.
     */
    @Schema(description = "EMI (ID) of the loan that was paid", example = "1")
    private long emiId;

    /**
     * Unique transaction identifier for this payment.
     */
    @Schema(description = "EMI Payment transaction UUID", example = "3d3a5d43-4464-41df-97db-f3b7d0f9eb2b")
    private String transactionUUID;

    /**
     * Amount of EMI that was paid (principle + interest if applicable).
     */
    @Schema(description = "EMI principle amount paid", example = "4050.34")
    private double amount;

    /**
     * The updated status of the EMI after payment.
     *
     * <p>Possible values are {@link EMIStatus#PAID}, {@link EMIStatus#PENDING}, etc.</p>
     */
    @Schema(description = "EMI status after payment", example = "PAID")
    private EMIStatus status;
}
