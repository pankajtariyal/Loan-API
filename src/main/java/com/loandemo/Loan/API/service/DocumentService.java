package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.document.DocumentDto;
import com.loandemo.Loan.API.dto.document.LoanWithDocument;
import com.loandemo.Loan.API.dto.document.VerifyDocumentRequest;
import com.loandemo.Loan.API.modul.Document;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.DocumentRepository;
import com.loandemo.Loan.API.repository.LoanRepository;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import com.loandemo.Loan.API.status.LoanStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling document-related operations
 * in the Loan Management System.
 *
 * <p>This service manages:</p>
 * <ul>
 *     <li>Uploading documents for a loan</li>
 *     <li>Re-uploading rejected documents</li>
 *     <li>Fetching loan details along with documents</li>
 *     <li>Downloading documents</li>
 *     <li>Verifying documents (admin operation)</li>
 * </ul>
 *
 * <p>Business Logic:</p>
 * <ul>
 *     <li>Only PDF and JPG files are allowed</li>
 *     <li>Maximum file size allowed is 5MB</li>
 *     <li>Each loan must have all required {@link DocumentType}</li>
 *     <li>Loan status changes based on document verification</li>
 * </ul>
 *
 * <p>Loan Status Flow based on documents:</p>
 * <pre>
 * All documents uploaded → UNDER_REVIEW
 * All documents VERIFIED → APPROVED → EMI generation
 * Any document REJECTED → REJECTED
 * </pre>
 *
 * @author Abhishek Tadiwal
 */
@Service
public class DocumentService {
    /**
     * Repository for user-related database operations.
     */
    private final UserRepository userRepository;

    /**
     * Repository for document-related database operations.
     */
    private final DocumentRepository documentRepository;

    /**
     * Repository for loan-related database operations.
     */
    private final LoanRepository loanRepository;

    /**
     * Service responsible for EMI generation after loan approval.
     */
    private final EMIService emiService;

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository user repository
     * @param documentRepository document repository
     * @param loanRepository loan repository
     * @param emiService EMI service
     */
    public DocumentService(UserRepository userRepository,
                           DocumentRepository documentRepository,
                           LoanRepository loanRepository,
                           EMIService emiService){
        this.documentRepository = documentRepository;
        this.loanRepository = loanRepository;
        this.emiService = emiService;
        this.userRepository = userRepository;
    }

    /**
     * Uploads or re-uploads a document for a specific loan.
     *
     * <p>This method performs the following:</p>
     * <ul>
     *     <li>Validates file type (PDF/JPG)</li>
     *     <li>Validates file size (max 5MB)</li>
     *     <li>Checks if document already exists</li>
     *     <li>Allows re-upload only if previous document was rejected</li>
     *     <li>Saves file to local storage</li>
     *     <li>Updates loan status to UNDER_REVIEW when all documents are uploaded</li>
     * </ul>
     *
     * @param loanId ID of the loan
     * @param type document type (string representation of {@link DocumentType})
     * @param file uploaded file
     * @return success message
     */
    @Transactional
    public String uploadDocument(Long loanId,String type, MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found."));

        Loan loan =loanRepository.findByIdAndUserUserId(loanId,user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("No loan found for user"));

        String uploadDir = "upload/loan-" + loanId;
        try {
            if (!file.getContentType().equals("image/jpeg") &&
                    !file.getContentType().equals("application/pdf")) {
                throw new RuntimeException("Only PDF or JPG allowed");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                System.out.println(file.getSize());
                throw new RuntimeException("File too large");
            }

            DocumentType documentType = DocumentType.valueOf(type.toUpperCase());
//            if(documentRepository.existsByLoanIdAndStatusNot(loanId,documentType,DocumentStatus.REJECTED)){
//                throw new IllegalArgumentException("Document already exist");
//            }
            Optional<Document> documentOptional = documentRepository.findByLoanIdAndDocumentType(loanId,documentType);
            if(documentOptional.isPresent()){
                Document document = documentOptional.get();
                if(document.getStatus()!= DocumentStatus.REJECTED){
                    throw new IllegalArgumentException("Document already exist");
                }

                Files.createDirectories(Paths.get(uploadDir));
                String fileName = UUID.randomUUID()+"_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                Path path = Paths.get(uploadDir,fileName);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                document.setFilePath(path.toString());
                document.setStatus(DocumentStatus.PENDING);
                document.setRejectedReason(null);
                documentRepository.save(document);
                return "Document reuploaded successfully";
            }
//            Loan loan = loanRepository.findById(loanId)
//                    .orElseThrow(()->new IllegalArgumentException("Loan does not found."));

            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID()+"_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path path = Paths.get(uploadDir,fileName);
            Files.copy(file.getInputStream(),path);

            Document document = Document.builder()
                    .loan(loan)
                    .documentType(DocumentType.valueOf(type.toUpperCase()))
                    .filePath(path.toString())
                    .status(DocumentStatus.PENDING)
                    .build();

            documentRepository.save(document);
            int count = documentRepository.countByLoanIdAndStatus(loanId,DocumentStatus.REJECTED);
            if(count== DocumentType.values().length){
                loan.setStatus(LoanStatus.UNDER_REVIEW);
                loanRepository.save(loan);
            }
            return "Document Uploaded Successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all loans along with their associated documents.
     *
     * <p>This method maps {@link Loan} entities into {@link LoanWithDocument}
     * DTOs including document details.</p>
     *
     * @return list of loans with document details
     */
    public List<LoanWithDocument> getAllDocument(){
        List<Loan> documentList = loanRepository.findAllLoanWithDocument();

        return documentList.stream()
                .map(loan->{
                    LoanWithDocument loanWithDocument = LoanWithDocument.builder()
                            .loan_id(loan.getId())
                            .amount(loan.getAmount())
                            .interestRate(loan.getInterestRate())
                            .tenureMonths(loan.getTenureMonths())
                            .status(loan.getStatus())
                            .createdAt(loan.getCreatedAt())
                            .build();
                    List<DocumentDto> documentDtoList = loan.getDocumentList().stream()
                            .map(document->{
                                return DocumentDto.builder()
                                        .id(document.getId())
                                        .documentType(document.getDocumentType())
                                        .status(document.getStatus())
                                        .rejectedReason(document.getRejectedReason())
                                        .uploadedAt(document.getUploadedAt())
                                        .build();
                            })
                            .collect(Collectors.toList());
                    loanWithDocument.setDocumentList(documentDtoList);
                    return loanWithDocument;
                })
                .collect(Collectors.toList());
    }


    /**
     * Downloads a document file associated with a loan.
     *
     * <p>This method:</p>
     * <ul>
     *     <li>Fetches document by loan ID and document ID</li>
     *     <li>Loads file from storage</li>
     *     <li>Returns file as a downloadable resource</li>
     * </ul>
     *
     * @param loanId ID of the loan
     * @param documentId ID of the document
     * @return {@link ResponseEntity} containing file resource
     */
    public ResponseEntity<Resource> download(Long loanId, Long documentId) {

        Document document  = documentRepository.findByIdAndLoanId(documentId,loanId)
                .orElseThrow(()->new IllegalArgumentException("Document Not Found"));

        Path path = Paths.get(document.getFilePath()).toAbsolutePath();
        try {
             Resource resource = new UrlResource(path.toUri());
             if(!resource.exists()){
                 throw new RuntimeException("File not exist");
             }
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType="application/octet-stream";
            }

            return ResponseEntity.ok()
                     .contentType(MediaType.parseMediaType(contentType))
                     .header(HttpHeaders.CONTENT_DISPOSITION,
                             "attachment; filename=\"" +
                                     resource.getFilename() + "\"")
                     .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies or rejects a document (admin operation).
     *
     * <p>This method performs the following:</p>
     * <ul>
     *     <li>Checks if document is in PENDING state</li>
     *     <li>Updates document status (VERIFIED/REJECTED)</li>
     *     <li>Updates loan status based on document verification:</li>
     *     <ul>
     *         <li>All VERIFIED → Loan APPROVED → EMI generated</li>
     *         <li>Any REJECTED → Loan REJECTED</li>
     *     </ul>
     * </ul>
     *
     * @param id document ID
     * @param documentRequest request containing status and rejection reason
     * @return status update message
     */
    @Transactional
    public String verifyDocument(Long id, VerifyDocumentRequest documentRequest) {
        Document document = documentRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Document not found"));

        if(!document.getStatus().equals(DocumentStatus.PENDING)){
            return "Document is not in PENDING state";
        }

        document.setStatus(DocumentStatus.valueOf(documentRequest.getStatus().toUpperCase()));
        document.setRejectedReason(documentRequest.getRejectedReason());

        documentRepository.save(document);
        Loan loan = document.getLoan();

        Long loanId = loan.getId();
        int count = documentRepository.countByLoanIdAndStatus(loanId,DocumentStatus.VERIFIED);
        int countRejection = documentRepository.countByLoanIdAndStatus(loanId,DocumentStatus.REJECTED);
        if(count == DocumentType.values().length){
            loan.setStatus(LoanStatus.APPROVED);
            loanRepository.save(loan);
            emiService.generateEMISchedule(loan);
        }else if(countRejection>0){
            loan.setStatus(LoanStatus.REJECTED);
            loan.setRejected_reason(documentRequest.getRejectedReason());
            loanRepository.save(loan);
        }

        return "Document Status changed successfully";
    }
}
