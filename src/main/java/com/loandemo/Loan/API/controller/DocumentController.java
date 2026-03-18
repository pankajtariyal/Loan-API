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

/**
 * REST Controller responsible for handling Loan Document operations.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>Fetching loan documents (Admin only)</li>
 *     <li>Downloading/viewing uploaded documents</li>
 *     <li>Verifying documents (Admin only)</li>
 * </ul>
 *
 * <p>All endpoints require JWT authentication.
 *
 * @apiNote Admin privileges are required for document verification and fetching all documents.
 *
 * @implNote This controller delegates document-related business logic to {@link DocumentService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "Document APIs",
        description = "Operations related to loan documents such as upload, verification, and retrieval"
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    /**
     * Fetches all loans along with their associated documents.
     *
     * <p>This API is restricted to administrators and returns
     * a list of all loans with uploaded documents.
     *
     * @return {@link APIResponse} containing list of {@link LoanWithDocument}
     *
     * @apiNote Accessible only by users with ADMIN role.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Fetches all loan records</li>
     *     <li>Maps associated documents</li>
     *     <li>Returns aggregated loan-document data</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get all documents",
            description = "Fetch all loan documents (Admin only)"
    )
    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("all")
    public ResponseEntity<APIResponse<List<LoanWithDocument>>> getAllLoanWithDocument(){
        List<LoanWithDocument> response = documentService.getAllDocument();
        return ResponseEntity.ok(APIResponse.success(response,"success"));
    }

    /**
     * Downloads or views a specific document for a given loan.
     *
     * <p>This API allows users to access uploaded documents
     * such as PAN card, ID proof, or salary slip.
     *
     * @param loanId ID of the loan
     * @param documentId ID of the document
     * @return {@link Resource} representing the document file
     *
     * @apiNote The response may be streamed as a file download or inline view.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Validates loan and document IDs</li>
     *     <li>Fetches file from storage</li>
     *     <li>Returns file as resource</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Download/View document",
            description = "Download or view a document using loan ID and document ID"
    )
    @GetMapping("{loanId}/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(
            @Parameter(description = "Loan ID")
            @PathVariable("loanId") Long loanId,

            @Parameter(description = "Document ID")
            @PathVariable("documentId") Long documentId){

        return documentService.download(loanId, documentId);
    }

    /**
     * Verifies or updates the status of a document.
     *
     * <p>This API allows administrators to approve or reject
     * uploaded documents for a loan.
     *
     * @param id ID of the document
     * @param documentRequest request payload containing verification status and remarks
     * @return {@link APIResponse} containing verification result message
     *
     * @apiNote Accessible only by users with ADMIN role.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Validates document existence</li>
     *     <li>Updates verification status (APPROVED/REJECTED)</li>
     *     <li>Saves changes in database</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Verify document",
            description = "Approve or reject a document (Admin only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{documentId}/status")
    public ResponseEntity<APIResponse<String>> verifyDocument(
            @Parameter(description = "Document ID")
            @PathVariable("documentId") Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Document verification request payload",
                    required = true
            )
            VerifyDocumentRequest documentRequest){

        String result = documentService.verifyDocument(id, documentRequest);
        return ResponseEntity.ok(APIResponse.success(result,"success"));
    }

}
