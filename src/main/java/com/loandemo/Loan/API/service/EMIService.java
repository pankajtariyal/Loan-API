package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiRequest;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiResponse;
import com.loandemo.Loan.API.modul.EMISchedule;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.EMIRepository;
import com.loandemo.Loan.API.repository.LoanRepository;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.status.EMIStatus;
import com.loandemo.Loan.API.status.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Security;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EMIService {
    private final EMIRepository emiRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanEmiPaymentService loanEmiPaymentService;
    @Autowired
    public EMIService(EMIRepository emiRepository,LoanRepository loanRepository,
                      UserRepository userRepository, LoanEmiPaymentService loanEmiPaymentService){
        this.emiRepository = emiRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.loanEmiPaymentService = loanEmiPaymentService;
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

    @Transactional
    public PayEmiResponse payEmi(Long loanId, Long emiId, PayEmiRequest payEmiRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        Loan loan = loanRepository.findByIdAndUserUserId(loanId,user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("No loan found for user"));

        if(loan.getStatus()!=LoanStatus.APPROVED){
            throw new IllegalArgumentException("Loan is not approved yet");
        }
        EMISchedule emiSchedule = emiRepository.findByIdAndLoanId(emiId,loan.getId())
                .orElseThrow(()->new IllegalArgumentException("NO Emi found for loan"));

        if(emiSchedule.getStatus()==EMIStatus.PAID){
            throw new IllegalArgumentException("Emi Already paid");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate emiDate = emiSchedule.getEmiDate();

        if(currentDate.isAfter(emiDate)){
            throw new IllegalArgumentException("EMI cannot be paid before due date");
        }

        if(payEmiRequest.getAmount()!=emiSchedule.getPrincipleAmount()) {
            throw new IllegalArgumentException("Emi amount does not matched paid amount");
        }
        String transaction = loanEmiPaymentService.makePayment(loan,emiSchedule,payEmiRequest);

        emiSchedule.setStatus(EMIStatus.PAID);
        emiRepository.save(emiSchedule);

        int emiCount = emiRepository.countEmiStatus(loan.getId(),EMIStatus.PENDING);
        if(emiCount==0){
            loan.setStatus(LoanStatus.CLOSED);
            loanRepository.save(loan);
        }
        return PayEmiResponse.builder()
                .loanId(loan.getId())
                .emiId(emiSchedule.getId())
                .transactionUUID(transaction)
                .amount(payEmiRequest.getAmount())
                .status(EMIStatus.PAID)
                .build();
    }
}
