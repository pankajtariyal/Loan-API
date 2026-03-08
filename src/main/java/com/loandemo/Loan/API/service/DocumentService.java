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

@Service
public class DocumentService {
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final LoanRepository loanRepository;
    private final EMIService emiService;

    public DocumentService(UserRepository userRepository, DocumentRepository documentRepository,LoanRepository loanRepository, EMIService emiService){
        this.documentRepository = documentRepository;
        this.loanRepository = loanRepository;
        this.emiService = emiService;
        this.userRepository = userRepository;
    }

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
            int count = documentRepository.countByLoanIdAndStatus(loanId);
            if(count== DocumentType.values().length){
                loan.setStatus(LoanStatus.UNDER_REVIEW);
                loanRepository.save(loan);
            }
            return "Document Uploaded Successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
