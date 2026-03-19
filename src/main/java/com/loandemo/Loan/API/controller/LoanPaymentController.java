package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.LoanEmiPaymentService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller responsible for handling Loan EMI Payment Transactions.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>Fetching EMI payment transactions for a specific loan</li>
 * </ul>
 *
 * <p>All endpoints require JWT authentication.
 *
 * @apiNote Users can only access transactions related to their own loans.
 *
 * @implNote This controller delegates payment-related logic to {@link LoanEmiPaymentService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "Payment APIs",
        description = "Operations related to EMI payment transactions"
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/emi/transaction")
public class LoanPaymentController {

    private static final Logger logger = LoggerFactory.getLogger(LoanPaymentController.class);

    private final LoanEmiPaymentService loanEmiPaymentService;

    public LoanPaymentController(LoanEmiPaymentService loanEmiPaymentService){
        this.loanEmiPaymentService = loanEmiPaymentService;
    }

    /**
     * Fetches all EMI payment transactions for a specific loan.
     *
     * <p>This API allows users to view the complete payment history
     * of a loan, including paid and pending EMI details.
     *
     * @param id ID of the loan
     * @return {@link APIResponse} containing list of {@link ViewTransaction}
     *
     * @apiNote The user must be authenticated and authorized to access the loan.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates loan ownership</li>
     *     <li>Fetches all EMI payment records</li>
     *     <li>Maps data into transaction response DTO</li>
     *     <li>Returns list of transactions</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get EMI Transactions",
            description = "Fetch all EMI transactions for a specific loan"
    )
    @GetMapping("{loanId}")
    public ResponseEntity<APIResponse<List<ViewTransaction>>> getAllEmiTransaction(
            @Parameter(description = "Loan ID")
            @PathVariable("loanId") Long id){
        logger.info("Get request to retrieve emi transaction history of loan id: {} from user: {}",id, SecurityUtil.getCurrentUser());
        List<ViewTransaction> response = loanEmiPaymentService.getAllPaymentByLoanId(id);
        logger.info("Retrieve emi transaction history successfully of loan id: {} from user: {}",id, SecurityUtil.getCurrentUser());
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }
}
