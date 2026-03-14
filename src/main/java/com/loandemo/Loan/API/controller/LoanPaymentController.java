package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.LoanEmiPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("loan/emi/transaction")
public class LoanPaymentController {

    private final LoanEmiPaymentService loanEmiPaymentService;
    public LoanPaymentController(LoanEmiPaymentService loanEmiPaymentService){
        this.loanEmiPaymentService = loanEmiPaymentService;
    }

    @GetMapping("{loanId}")
    public ResponseEntity<APIResponse<List<ViewTransaction>>> getAllEmiTransaction(@PathVariable("loanId")Long id){
        List<ViewTransaction> response = loanEmiPaymentService.getAllPaymentByLoanId(id);
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }
}
