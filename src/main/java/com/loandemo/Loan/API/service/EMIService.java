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

/**
 * Service class responsible for handling EMI (Equated Monthly Installment)
 * related operations in the Loan Management System.
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Generate EMI schedule after loan approval</li>
 *     <li>Fetch EMI details for a loan</li>
 *     <li>Handle EMI payments</li>
 * </ul>
 *
 * <p>Business Flow:</p>
 * <pre>
 * Loan Approved →
 * EMI Schedule Generated →
 * User Pays EMI →
 * EMI Status Updated →
 * All EMIs Paid → Loan Closed
 * </pre>
 *
 * <p>EMI Formula Used:</p>
 * EMI = [P × R × (1+R)^N] / [(1+R)^N - 1]
 * where:
 * P = Principal Amount
 * R = Monthly Interest Rate
 * N = Number of Months
 *
 * @author Abhishek Tadiwal
 */
@Service
public class EMIService {
    /**
     * Repository for EMI schedule operations.
     */
    private final EMIRepository emiRepository;

    /**
     * Repository for loan operations.
     */
    private final LoanRepository loanRepository;

    /**
     * Repository for user operations.
     */
    private final UserRepository userRepository;

    /**
     * Service responsible for handling EMI payments.
     */
    private final LoanEmiPaymentService loanEmiPaymentService;

    /**
     * Constructor for dependency injection.
     *
     * @param emiRepository EMI repository
     * @param loanRepository Loan repository
     * @param userRepository User repository
     * @param loanEmiPaymentService EMI payment service
     */
    public EMIService(EMIRepository emiRepository,
                      LoanRepository loanRepository,
                      UserRepository userRepository,
                      LoanEmiPaymentService loanEmiPaymentService){
        this.emiRepository = emiRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.loanEmiPaymentService = loanEmiPaymentService;
    }

    /**
     * Generates EMI schedule for an approved loan.
     *
     * <p>This method calculates EMI using standard formula and creates
     * monthly EMI entries including:</p>
     * <ul>
     *     <li>EMI amount</li>
     *     <li>Interest component</li>
     *     <li>Principal component</li>
     *     <li>Remaining balance</li>
     *     <li>Due date</li>
     * </ul>
     *
     * <p>Each EMI is stored in the database.</p>
     *
     * @param loan loan for which EMI schedule is generated
     */
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

    /**
     * Processes EMI payment for a specific loan and EMI.
     *
     * <p>This method performs multiple validations:</p>
     * <ul>
     *     <li>Checks if user is authenticated</li>
     *     <li>Validates loan ownership</li>
     *     <li>Ensures loan is APPROVED</li>
     *     <li>Ensures EMI is not already PAID</li>
     *     <li>Validates payment date and amount</li>
     * </ul>
     *
     * <p>On successful payment:</p>
     * <ul>
     *     <li>Payment is processed via payment service</li>
     *     <li>EMI status is updated to PAID</li>
     *     <li>If all EMIs are paid → Loan status is set to CLOSED</li>
     * </ul>
     *
     * @param loanId loan ID
     * @param emiId EMI ID
     * @param payEmiRequest payment request containing amount and details
     * @return {@link PayEmiResponse} containing transaction details
     * @throws Exception if validation fails or payment error occurs
     */
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
            throw new IllegalArgumentException("EMI cannot be paid because due date must include due penalty");
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
