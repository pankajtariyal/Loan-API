package com.loandemo.Loan.API.dto.emiDto;

import com.loandemo.Loan.API.status.EMIStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewEmi {
    private int emiNumber;
    private LocalDate emiDate;
    private double emiAmount;
    private double interestAmount;
    private double principle;
    private EMIStatus status;
}
