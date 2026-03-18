package com.loandemo.Loan.API.dto.document;

import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a document associated with a loan.
 *
 * <p>This DTO is used to return document details to the client, including
 * its type, status, rejection reason (if any), and upload timestamp.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Document Response Modal")
public class DocumentDto {

    /**
     * Unique identifier of the document.
     */
    @Schema(description = "Document ID", example = "1")
    private Long id;

    /**
     * Type of the document.
     *
     * <p>Possible values are {@link DocumentType#PAN}, {@link DocumentType#ID_PROOF},
     * {@link DocumentType#SALARY_SLIP}, etc.</p>
     */
    @Schema(description = "Document type", example = "PAN")
    private DocumentType documentType;

    /**
     * Status of the document.
     *
     * <p>Possible values are {@link DocumentStatus#PENDING}, {@link DocumentStatus#UNDER_REVIEW},
     * {@link DocumentStatus#VERIFIED}, {@link DocumentStatus#REJECTED}.</p>
     */
    @Schema(description = "Document Status", example = "PENDING")
    private DocumentStatus status;

    /**
     * Reason for rejection if the document is rejected.
     */
    @Schema(description = "Document rejection reason", example = "Document is unclear")
    private String rejectedReason;

    /**
     * Timestamp when the document was uploaded.
     */
    @Schema(description = "Document upload timestamp (in date and time)", example = "2026-02-25 22:23:57.021")
    private LocalDateTime uploadedAt;
}
