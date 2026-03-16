package com.loandemo.Loan.API.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Document verification modal")
public class VerifyDocumentRequest {

    @Schema(description = "Document status to be",example = "REJECTED")
    @NotBlank
    private String status;
    @Schema(description = "Document rejection reason", example = "Document is blur")
    private String rejectedReason;
}
