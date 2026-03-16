package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.document.LoanWithDocument;
import com.loandemo.Loan.API.dto.document.VerifyDocumentRequest;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name="Document APIs", description = "Operation related document like verify document and other.")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @Operation(summary = "Get all document", description = "Document fetch for specific loan and only admin can access it")
    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("all")
    public ResponseEntity<APIResponse<List<LoanWithDocument>>> getAllLoanWithDocument(){
        List<LoanWithDocument> resposne = documentService.getAllDocument();
        return ResponseEntity.ok(APIResponse.success(resposne,"success"));
    }

    @Operation(summary = "Download or view document", description = "Download or view document by document id and loan id")
    @GetMapping({"{loanId}/download/{documentId}"})
    public ResponseEntity<Resource> downloadDocument(
            @Parameter(description = "Loan id") @PathVariable("loanId")Long loanId,
            @Parameter(description = "document id") @PathVariable("documentId")Long documentId){
       return documentService.download(loanId,documentId);
    }

    @Operation(summary = "Verify document", description = "Document verify and Admin access")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{documentId}/status")
    public ResponseEntity<APIResponse<String>> verifyDocument(
            @Parameter(description = "Loan id") @PathVariable("documentId")Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Document verify request body",
                    required = true
            ) VerifyDocumentRequest documentRequest){
        String result = documentService.verifyDocument(id,documentRequest);
        return ResponseEntity.ok(APIResponse.success(result,"success"));
    }

}
