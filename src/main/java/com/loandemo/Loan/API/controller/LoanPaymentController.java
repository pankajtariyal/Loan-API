package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.LoanEmiPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name="Payment APIs",
        description = "Operation related to Emi payment")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/emi/transaction")
public class LoanPaymentController {

    private final LoanEmiPaymentService loanEmiPaymentService;
    public LoanPaymentController(LoanEmiPaymentService loanEmiPaymentService){
        this.loanEmiPaymentService = loanEmiPaymentService;
    }

    @Operation(summary = "Get EMI Transaction", description = "Customer to get their loan emi transaction ")
    @GetMapping("{loanId}")
    public ResponseEntity<APIResponse<List<ViewTransaction>>> getAllEmiTransaction(
            @Parameter(description = "Loan id") @PathVariable("loanId")Long id){
        List<ViewTransaction> response = loanEmiPaymentService.getAllPaymentByLoanId(id);
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }
}
