package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiRequest;
import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.modul.EMISchedule;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.modul.LoanPayment;
import com.loandemo.Loan.API.repository.LoanPaymentRepository;
import com.loandemo.Loan.API.status.PaymentMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for handling EMI payment transactions
 * in the Loan Management System.
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Process EMI payments</li>
 *     <li>Generate unique transaction IDs</li>
 *     <li>Store payment details in the database</li>
 *     <li>Fetch transaction history for a loan</li>
 * </ul>
 *
 * <p>Payment Flow:</p>
 * <pre>
 * User Initiates EMI Payment →
 * Validate Payment Details →
 * Generate Transaction ID →
 * Save Payment Record →
 * Return Transaction Details
 * </pre>
 *
 * <p>Each payment is associated with:</p>
 * <ul>
 *     <li>A Loan</li>
 *     <li>An EMI Schedule entry</li>
 *     <li>Payment amount</li>
 *     <li>Payment mode</li>
 *     <li>Unique transaction ID</li>
 * </ul>
 *
 * @author Abhishek
 */
@Service
public class LoanEmiPaymentService {

    /**
     * Repository for handling loan payment persistence operations.
     */
    private final LoanPaymentRepository loanPaymentRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param loanPaymentRepository repository for loan payments
     */
    public LoanEmiPaymentService(LoanPaymentRepository loanPaymentRepository){
        this.loanPaymentRepository = loanPaymentRepository;
    }

    /**
     * Processes EMI payment and stores transaction details.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Generates a unique transaction ID using UUID</li>
     *     <li>Creates a {@link LoanPayment} entity</li>
     *     <li>Maps payment mode from request</li>
     *     <li>Saves payment record in the database</li>
     * </ul>
     *
     * <p>If any error occurs during payment processing, an exception is thrown.</p>
     *
     * @param loan the loan associated with the payment
     * @param emiSchedule the EMI schedule entry being paid
     * @param emiRequest request containing payment details
     * @return generated transaction ID
     * @throws Exception if any error occurs during payment processing
     */
    @Transactional
    public String makePayment(Loan loan, EMISchedule emiSchedule, PayEmiRequest emiRequest) throws Exception {
        try{
            String transactionId = UUID.randomUUID().toString();
            LoanPayment loanPayment = LoanPayment.builder()
                    .loan(loan)
                    .emi(emiSchedule)
                    .amount(emiRequest.getAmount())
                    .transactionId(transactionId)
                    .paymentMode(PaymentMode.valueOf(emiRequest.getPaymentMode().toUpperCase()))
                    .build();
            loanPaymentRepository.save(loanPayment);
            return transactionId;
        }catch (Exception e){
            throw new Exception("Error occur making transaction");
        }
    }

    /**
     * Retrieves all payment transactions for a given loan ID.
     *
     * <p>This method fetches transaction details directly mapped
     * into {@link ViewTransaction} DTO.</p>
     *
     * @param loanId ID of the loan
     * @return list of transaction details
     */
    public List<ViewTransaction> getAllPaymentByLoanId(Long loanId){
        return loanPaymentRepository.findByLoanId(loanId);
    }
}
