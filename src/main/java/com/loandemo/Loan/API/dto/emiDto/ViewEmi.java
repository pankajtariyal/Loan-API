package com.loandemo.Loan.API.dto.emiDto;

import com.loandemo.Loan.API.status.EMIStatus;
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
@Schema(description = "EMI modal response of get all emi")
public class ViewEmi {
    @Schema(description = "EMI number",example = "1")
    private int emiNumber;
    @Schema(description = "EMI date", example = "21-11-2025")
    private LocalDate emiDate;
    @Schema(description = "EMI Amount to be paid",example = "4034.55")
    private double emiAmount;
    @Schema(description = "EMI interest amount", example = "1.223")
    private double interestAmount;
    @Schema(description = "Loan principle amount", example = "50000")
    private double principle;
    @Schema(description = "EMI status",example = "PAID/PENDING")
    private EMIStatus status;
}
