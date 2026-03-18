package com.loandemo.Loan.API.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Data Transfer Object used to verify a loan document.
 *
 * <p>This DTO is sent by the admin or verification system to update the
 * status of a specific document and optionally provide a rejection reason.</p>
 *
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Document verification modal")
public class VerifyDocumentRequest {

    /**
     * Status to update the document to.
     *
     * <p>Possible values include {@code REJECTED}, {@code VERIFIED}, etc.</p>
     */
    @Schema(description = "Document status to be", example = "REJECTED")
    @NotBlank(message = "Document status is required")
    private String status;

    /**
     * Reason for document rejection.
     *
     * <p>This field is optional and should only be set if the document
     * status is {@code REJECTED}.</p>
     */
    @Schema(description = "Document rejection reason", example = "Document is blur")
    private String rejectedReason;
}
