package com.loandemo.Loan.API.dto.document;

import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private Long id;
    private DocumentType documentType;
    private DocumentStatus status;
    private String rejectedReason;
    private LocalDateTime uploadedAt;
}
