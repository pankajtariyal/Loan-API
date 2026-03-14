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

@Service
public class LoanEmiPaymentService {
    private final LoanPaymentRepository loanPaymentRepository;
    public LoanEmiPaymentService(LoanPaymentRepository loanPaymentRepository){
        this.loanPaymentRepository = loanPaymentRepository;
    }

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

    public List<ViewTransaction> getAllPaymentByLoanId(Long loanId){
        List<ViewTransaction> loanPayments = loanPaymentRepository.findByLoanId(loanId);
        return loanPayments;
    }
}
