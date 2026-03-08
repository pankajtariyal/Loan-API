package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.modul.EMISchedule;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.repository.EMIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EMIService {
    private final EMIRepository emiRepository;

    @Autowired
    public EMIService(EMIRepository emiRepository){
        this.emiRepository = emiRepository;
    }

    public void generateEMISchedule(Loan loan) {

        BigDecimal principal = BigDecimal.valueOf(loan.getAmount());
        BigDecimal annualRate = BigDecimal.valueOf(loan.getInterestRate());
        int tenureMonths = loan.getTenureMonths();

        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRPowerN = BigDecimal
                .valueOf(Math.pow(
                        BigDecimal.ONE.add(monthlyRate).doubleValue(),
                        tenureMonths));

        BigDecimal emi = principal
                .multiply(monthlyRate)
                .multiply(onePlusRPowerN)
                .divide(onePlusRPowerN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        LocalDate dueDate = LocalDate.now().plusMonths(1);

        List<EMISchedule> list = new ArrayList<>();

        for (int i = 1; i <= tenureMonths; i++) {

            BigDecimal interest = principal
                    .multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPaid = emi
                    .subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP);

            principal = principal
                    .subtract(principalPaid)
                    .setScale(2, RoundingMode.HALF_UP);

            if (i == tenureMonths) {
                principal = BigDecimal.ZERO;
            }

            EMISchedule emiSchedule = EMISchedule.builder()
                    .loan(loan)
                    .emiNumber(i)
                    .emiDate(dueDate)
                    .principleAmount(principalPaid.doubleValue())
                    .interestAmount(interest.doubleValue())
                    .emiAmount(emi.doubleValue())
                    .remainingBalance(principal.doubleValue())
                    .build();

            dueDate = dueDate.plusMonths(1);

            list.add(emiSchedule);

            System.out.println(emiSchedule);
        }

        emiRepository.saveAll(list);
    }

    public List<ViewEmi> getAllEmiByLoanId(long id){
        List<EMISchedule> emiList = emiRepository.findByLoanIdOrderByEmiNumber(id);

        List<ViewEmi> viewEmiList = emiList.stream()
                .map(emi->{
                    return ViewEmi.builder()
                            .emiNumber(emi.getEmiNumber())
                            .emiDate(emi.getEmiDate())
                            .emiAmount(emi.getEmiAmount())
                            .interestAmount(emi.getInterestAmount())
                            .principle(emi.getPrincipleAmount())
                            .status(emi.getStatus())
                            .build();
                }).collect(Collectors.toList());
        return viewEmiList;
    }

}
