package com.loandemo.Loan.API.dto.emiDto;

import com.loandemo.Loan.API.status.EMIStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object representing the details of an EMI for a loan.
 *
 * <p>This DTO is used to return EMI information in responses when fetching
 * all EMIs for a specific loan.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "EMI model response for retrieving all EMIs of a loan")
public class ViewEmi {

    /**
     * EMI number in the sequence of payments for the loan.
     */
    @Schema(description = "EMI number", example = "1")
    private int emiNumber;

    /**
     * The due date for the EMI.
     */
    @Schema(description = "EMI date", example = "21-11-2025")
    private LocalDate emiDate;

    /**
     * Total EMI amount to be paid for this installment.
     */
    @Schema(description = "EMI Amount to be paid", example = "4034.55")
    private double emiAmount;

    /**
     * Portion of the EMI amount that goes towards interest.
     */
    @Schema(description = "EMI interest amount", example = "1.223")
    private double interestAmount;

    /**
     * Portion of the EMI amount that goes towards principal repayment.
     */
    @Schema(description = "Loan principal amount", example = "50000")
    private double principle;

    /**
     * Status of the EMI payment.
     *
     * <p>Possible values:
     * <ul>
     *     <li>PAID</li>
     *     <li>PENDING</li>
     * </ul>
     * </p>
     */
    @Schema(description = "EMI status", example = "PAID/PENDING")
    private EMIStatus status;
}
