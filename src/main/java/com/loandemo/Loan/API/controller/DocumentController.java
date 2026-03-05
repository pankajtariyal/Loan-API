package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.document.LoanWithDocument;
import com.loandemo.Loan.API.dto.document.VerifyDocumentRequest;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("loan/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("all")
    public ResponseEntity<APIResponse<List<LoanWithDocument>>> getAllLoanWithDocument(){
        List<LoanWithDocument> resposne = documentService.getAllDocument();
        return ResponseEntity.ok(APIResponse.success(resposne,"success"));
    }

    @GetMapping({"{loanId}/download/{documentId}"})
    public ResponseEntity<Resource> downloadDocument(@PathVariable("loanId")Long loanId,@PathVariable("documentId")Long documentId){
       return documentService.download(loanId,documentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{documentId}/status")
    public ResponseEntity<APIResponse<String>> verifyDocument(@PathVariable("documentId")Long id, @Valid @RequestBody VerifyDocumentRequest documentRequest){
        String result = documentService.verifyDocument(id,documentRequest);
        return ResponseEntity.ok(APIResponse.success(result,"success"));
    }

}
