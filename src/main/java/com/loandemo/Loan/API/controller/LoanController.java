package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyRequest;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByIdResponse;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByUserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import com.loandemo.Loan.API.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Tag(
        name="Loan APIs",
        description = "Operation related to loans")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loans")
public class LoanController {

    private final LoanService loanService;
    private final DocumentService documentService;
    public LoanController(LoanService loanService,DocumentService documentService){
        this.loanService = loanService;
        this.documentService = documentService;
    }

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
        LoanApplyResponse response = loanService.applyLoan(loanApplyRequest);
        return ResponseEntity.ok(APIResponse.success(response,"Loan Application Submitted Successfully"));
    }

    @Operation(
            summary = "Get All Loan",
            description = "Loan list customer specific"
    )
    @GetMapping("my")
    public ResponseEntity<APIResponse<GetLoanByUserResponse>> getLoans(){
        GetLoanByUserResponse response = loanService.getLoan();
        return ResponseEntity.ok(APIResponse.success(response,"Loan List"));
    }

    @Operation(
            summary = "Get Loan",
            description = "Only Admin can access"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<APIResponse<GetLoanByIdResponse>> getLoanById(
            @Parameter(description = "Loan ID") @PathVariable Long id){

        GetLoanByIdResponse getLoanByIdResponse = loanService.getLoanById(id);
        return ResponseEntity.ok(APIResponse.success(getLoanByIdResponse,"Loan"));
    }

    @Operation(
            summary = "Get All customer loan",
            description = "Only Admin can access"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get/all/loan")
    public ResponseEntity<APIResponse<List<GetAllLoan>>> getAllLoan(){
        List<GetAllLoan> getAllLoans = loanService.getAllLoan();
        return ResponseEntity.ok(APIResponse.success(getAllLoans,"Loan"));
    }

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
        String response = documentService.uploadDocument(loanId,documentType,file);
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }

}
