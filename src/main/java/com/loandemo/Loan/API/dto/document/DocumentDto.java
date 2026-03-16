package com.loandemo.Loan.API.dto.document;

import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Document Response Modal")
public class DocumentDto {
    @Schema(description = "document id",example = "1")
    private Long id;
    @Schema(description = "document type", example = "PAN")
    private DocumentType documentType;
    @Schema(description = "Document Status",example = "PENDING")
    private DocumentStatus status;
    @Schema(description = "Document rejection reason",example = "Document is unclear")
    private String rejectedReason;
    @Schema(description = "Document upload at( in date and time)",example = "2026-02-25 22:23:57.021")
    private LocalDateTime uploadedAt;
}
