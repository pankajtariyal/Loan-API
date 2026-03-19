package com.loandemo.Loan.API.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyRequest;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByIdResponse;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByUserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import com.loandemo.Loan.API.service.LoanService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller responsible for handling Loan related operations.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>Applying for a loan</li>
 *     <li>Fetching loans for the authenticated user</li>
 *     <li>Fetching loan details by ID (Admin only)</li>
 *     <li>Fetching all loans (Admin only)</li>
 *     <li>Uploading documents for a loan</li>
 * </ul>
 *
 * <p>All endpoints are secured using JWT-based authentication.
 *
 * @apiNote All APIs require a valid Bearer token in the Authorization header.
 *
 * @implNote This controller delegates business logic to {@link LoanService}
 * and {@link DocumentService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name="Loan APIs",
        description = "Operation related to loans")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loans")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;
    private final DocumentService documentService;
    public LoanController(LoanService loanService,DocumentService documentService){
        this.loanService = loanService;
        this.documentService = documentService;
    }

    /**
     * Creates a new loan application for the authenticated user.
     *
     * <p>This API accepts loan details such as amount, tenure,
     * and interest rate to create a new loan.
     *
     * @param loanApplyRequest request payload containing loan details
     * @return {@link APIResponse} containing {@link LoanApplyResponse}
     *
     * @apiNote The user must be authenticated with a valid JWT token.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates the request payload</li>
     *     <li>Creates a new loan entity</li>
     *     <li>Persists the loan in the database</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Apply Loan",
            description = "Creates a new loan for a customer"
    )
    @PostMapping("apply")
    public ResponseEntity<APIResponse<LoanApplyResponse>> applyLoan(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan request payload",
                    required = true
            ) LoanApplyRequest loanApplyRequest){
        logger.info("Post request apply loan from user: {}", SecurityUtil.getCurrentUser());
        try{
            LoanApplyResponse response = loanService.applyLoan(loanApplyRequest);
            logger.info("Post request apply loan proceed successfully from user: {}", SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(response,"Loan Application Submitted Successfully"));
        }catch (Exception e){
            logger.error("Failed to Apply loan from user: {}",SecurityUtil.getCurrentUser(),e);
            throw new IllegalArgumentException("Failed to apply loan");
        }
    }


    /**
     * Retrieves all loans associated with the authenticated user.
     *
     * <p>This API returns the list of loans belonging to the currently logged-in user.
     *
     * @return {@link APIResponse} containing {@link GetLoanByUserResponse}
     *
     * @apiNote The user must be authenticated with a valid JWT token.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Fetches the authenticated user</li>
     *     <li>Retrieves all loans associated with the user</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get All Loan",
            description = "Loan list customer specific"
    )
    @GetMapping("my")
    public ResponseEntity<APIResponse<GetLoanByUserResponse>> getLoans(){
        logger.info("GET request, Retrieve all loan of user: {}",SecurityUtil.getCurrentUser());
        GetLoanByUserResponse response = loanService.getLoan();
        logger.info("GET request, Retrieve all loan successfully of user: {}",SecurityUtil.getCurrentUser());
        return ResponseEntity.ok(APIResponse.success(response,"Loan List"));
    }

    /**
     * Retrieves loan details by loan ID.
     *
     * <p>This API is restricted to ADMIN users and returns detailed
     * information for a specific loan.
     *
     * @param id the unique identifier of the loan
     * @return {@link APIResponse} containing {@link GetLoanByIdResponse}
     *
     * @apiNote Only users with ADMIN role can access this endpoint.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Validates loan ID</li>
     *     <li>Fetches loan details from database</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get Loan",
            description = "Only Admin can access"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<APIResponse<GetLoanByIdResponse>> getLoanById(
            @Parameter(description = "Loan ID") @PathVariable Long id){
        logger.info("GET request, Retrieve loan by id: {} from admin: {}",id,SecurityUtil.getCurrentUser());
        try{
            GetLoanByIdResponse getLoanByIdResponse = loanService.getLoanById(id);
            logger.info("GET request,Loan retrieve successfully by id: {} from admin: {}",id,SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(getLoanByIdResponse,"Loan"));
        }catch (Exception e){
            logger.error("Failed to retrieve loan by id: {} from admin: {}",id,SecurityUtil.getCurrentUser(),e);
            throw new IllegalArgumentException("Failed to retrieve loan");
        }
    }

    /**
     * Retrieves all loan applications in the system.
     *
     * <p>This API is restricted to ADMIN users and returns
     * all loan records.
     *
     * @return {@link APIResponse} containing list of {@link GetAllLoan}
     *
     * @apiNote Only users with ADMIN role can access this endpoint.
     *
     * @implSpec This method fetches all loan records from the database.
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get All customer loan",
            description = "Only Admin can access"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get/all/loan")
    public ResponseEntity<APIResponse<List<GetAllLoan>>> getAllLoan(){
        logger.info("GET request, Retrieve all loan from admin: {}",SecurityUtil.getCurrentUser());
        List<GetAllLoan> getAllLoans = loanService.getAllLoan();
        logger.info("GET request, Retrieve all loan successfully from admin: {}",SecurityUtil.getCurrentUser());
        return ResponseEntity.ok(APIResponse.success(getAllLoans,"Loan"));
    }


    /**
     * Uploads a document for a specific loan.
     *
     * <p>This API allows users to upload documents such as:
     * <ul>
     *     <li>PAN Card</li>
     *     <li>ID Proof</li>
     *     <li>Salary Slip</li>
     * </ul>
     *
     * @param loanId the unique identifier of the loan
     * @param documentType type of the document (e.g., PAN, ID_PROOF, SALARY_SLIP)
     * @param file the document file to be uploaded
     *
     * @return {@link APIResponse} containing success message
     *
     * @throws IllegalArgumentException if file is empty
     *
     * @implSpec This method:
     * <ol>
     *     <li>Validates file input</li>
     *     <li>Uploads document</li>
     *     <li>Associates document with loan</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary="Upload loan document",
            description="Upload PAN, SALARY_SLIP or ID_PROOF"
    )
    @PostMapping(path = "{loan_id}/document", consumes = "multipart/form-data")
    public ResponseEntity<APIResponse<String>> uploadDocument(
            @Parameter(description = "Loan ID For uploading document if specific loan") @PathVariable("loan_id") Long loanId,
            @Parameter(description = "Document type",example = "PAN") @RequestParam("documentType")String documentType,
            @Parameter(description = "File") @RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty.");
        }
        logger.info("POST request to upload document of loan id: {} from user: {}",loanId,SecurityUtil.getCurrentUser());
        try{
            String response = documentService.uploadDocument(loanId,documentType,file);
            logger.info("Document uploaded successfully of loan id: {} from user: {}",loanId,SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(response,"success"));
        }catch (Exception e){
            logger.error("POST request failed of document upload of loan id: {} from user: {}",loanId,SecurityUtil.getCurrentUser());
            throw new IllegalArgumentException("Failed to upload document");
        }
    }

}
