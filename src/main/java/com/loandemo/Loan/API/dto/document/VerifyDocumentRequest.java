package com.loandemo.Loan.API.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyDocumentRequest {

    @NotBlank
    private String status;
    private String rejectedReason;
}
