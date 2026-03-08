package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyRequest;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByIdResponse;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByUserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import com.loandemo.Loan.API.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("loans")
public class LoanController {

    private final LoanService loanService;
    private final DocumentService documentService;
    public LoanController(LoanService loanService,DocumentService documentService){
        this.loanService = loanService;
        this.documentService = documentService;
    }

    @PostMapping("apply")
    public ResponseEntity<APIResponse<LoanApplyResponse>> applyLoan(@Valid @RequestBody LoanApplyRequest loanApplyRequest){
        LoanApplyResponse response = loanService.applyLoan(loanApplyRequest);
        return ResponseEntity.ok(APIResponse.success(response,"Loan Application Submitted Successfully"));
    }

    @GetMapping("my")
    public ResponseEntity<APIResponse<GetLoanByUserResponse>> getLoans(){
        GetLoanByUserResponse response = loanService.getLoan();
        return ResponseEntity.ok(APIResponse.success(response,"Loan List"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<APIResponse<GetLoanByIdResponse>> getLoanById(@PathVariable Long id){
        GetLoanByIdResponse getLoanByIdResponse = loanService.getLoanById(id);
        return ResponseEntity.ok(APIResponse.success(getLoanByIdResponse,"Loan"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("get/all/loan")
    public ResponseEntity<APIResponse<List<GetAllLoan>>> getAllLoan(){
        List<GetAllLoan> getAllLoans = loanService.getAllLoan();
        return ResponseEntity.ok(APIResponse.success(getAllLoans,"Loan"));
    }

    @PostMapping("{loan_id}/document")
    public ResponseEntity<APIResponse<String>> uploadDocument(
            @PathVariable("loan_id") Long loanId,
            @RequestParam("documentType")String documentType,
            @RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty.");
        }
        String response = documentService.uploadDocument(loanId,documentType,file);
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }

}
