package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiRequest;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiResponse;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByUserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import com.loandemo.Loan.API.service.EMIService;
import com.loandemo.Loan.API.service.LoanService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller responsible for handling EMI (Equated Monthly Installment) operations.
 *
 * <p>This controller provides endpoints for:
 * <ul>
 *     <li>Fetching EMI schedule for a specific loan</li>
 *     <li>Processing EMI payments</li>
 * </ul>
 *
 * <p>All endpoints are secured and require JWT-based authentication.
 *
 * @apiNote All APIs require a valid Bearer token in the Authorization header.
 *
 * @implNote This controller delegates all business logic to {@link EMIService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "EMI APIs",
        description = "Operations related to EMI management"
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/emi")
public class EMIController {
    private final static Logger logger = LoggerFactory.getLogger(EMIController.class);
    private final EMIService emiService;

    @Autowired
    public EMIController(EMIService emiService){
        this.emiService = emiService;
    }

    /**
     * Retrieves all EMI records associated with a specific loan.
     *
     * <p>This API allows an authenticated user to view the complete EMI schedule
     * of a loan using the loan ID.
     *
     * @param id the unique identifier of the loan
     * @return {@link APIResponse} containing a list of {@link ViewEmi} objects
     *
     * @apiNote The user must be authenticated and authorized to access the given loan.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates the loan ID</li>
     *     <li>Fetches all EMI records associated with the loan</li>
     *     <li>Returns the EMI schedule as a response</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get All EMI",
            description = "Fetch all EMIs associated with a specific loan ID"
    )
    @GetMapping("{id}/emi-schedule/get/all")
    public ResponseEntity<APIResponse<List<ViewEmi>>> getAllLoanEmi(
            @Parameter(description = "Loan ID") @PathVariable("id") Long id) {
        logger.info("Request to fetch all Loan EMI of user: {}", SecurityUtil.getCurrentUser());
        List<ViewEmi> list = emiService.getAllEmiByLoanId(id);
        logger.info("Proceed successfully to fetch all Loan EMI of user: {}", SecurityUtil.getCurrentUser());
        return ResponseEntity.ok(APIResponse.success(list));
    }

    /**
     * Processes payment for a specific EMI of a loan.
     *
     * <p>This API allows a user to pay an EMI installment by providing
     * the loan ID, EMI ID, and payment details.
     *
     * @param loanId the unique identifier of the loan
     * @param emiId the unique identifier of the EMI installment
     * @param emiRequest request payload containing payment details
     *
     * @return {@link APIResponse} containing {@link PayEmiResponse} with payment confirmation
     *
     * @throws Exception if payment processing fails or EMI is invalid
     *
     * @apiNote Only pending EMIs can be paid.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates loan ID and EMI ID</li>
     *     <li>Checks EMI payment status</li>
     *     <li>Processes payment</li>
     *     <li>Updates EMI status to PAID</li>
     *     <li>Stores payment details</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Pay EMI",
            description = "Pay a specific EMI for a loan"
    )
    @PostMapping("{loanId}/pay/{emiId}")
    public ResponseEntity<APIResponse<PayEmiResponse>> payEmiByIds(
            @Parameter(description = "EMI ID") @PathVariable("emiId") Long emiId,
            @Parameter(description = "Loan ID") @PathVariable("loanId") Long loanId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "EMI payment request payload",
                    required = true
            )
            @RequestBody PayEmiRequest emiRequest) throws Exception {
        logger.info("EMI pay request for loan: {} from user {}",loanId,SecurityUtil.getCurrentUser());
        try{
            PayEmiResponse response = emiService.payEmi(loanId, emiId, emiRequest);
            logger.info("EMI pay request proceed successfully for loan: {} from user {}",loanId,SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(response));
        }catch (IllegalAccessException e){
            logger.error("Emi payment failed of loan: {} from user: {}",loanId,SecurityUtil.getCurrentUser());
            throw new IllegalArgumentException("Failed to pay emi");
        }
    }
}

