package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a document associated with a loan.
 *
 * <p>Each document is linked to a specific {@link Loan} and represents
 * required verification documents such as:</p>
 * <ul>
 *     <li>PAN</li>
 *     <li>Salary Slip</li>
 *     <li>ID Proof</li>
 * </ul>
 *
 * <p>A loan can have multiple documents, but only one document per
 * {@link DocumentType} is allowed (enforced via unique constraint).</p>
 *
 * <p>This entity also tracks the verification status of the document
 * and supports optimistic locking using version control.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "loan_document", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"loan_id", "document_type"})
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    /**
     * Unique identifier for the document.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Loan to which this document belongs.
     */
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * Type of the document.
     *
     * <p>Possible values:</p>
     * <ul>
     *     <li>{@link DocumentType#PAN}</li>
     *     <li>{@link DocumentType#SALARY_SLIP}</li>
     *     <li>{@link DocumentType#ID_PROOF}</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 255, nullable = false)
    private DocumentType documentType;

    /**
     * File path where the document is stored.
     */
    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;

    /**
     * Current verification status of the document.
     *
     * <p>Possible values:</p>
     * <ul>
     *     <li>{@link DocumentStatus#PENDING}</li>
     *     <li>{@link DocumentStatus#UNDER_REVIEW}</li>
     *     <li>{@link DocumentStatus#VERIFIED}</li>
     *     <li>{@link DocumentStatus#REJECTED}</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentStatus status;

    /**
     * Reason for rejection (if status is REJECTED).
     */
    @Column(name = "rejected_reason")
    private String rejectedReason;

    /**
     * Timestamp when the document was uploaded.
     */
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    /**
     * Version field used for optimistic locking.
     *
     * <p>This helps prevent concurrent update conflicts.</p>
     */
    @Version
    private int version;

    /**
     * Lifecycle callback executed before persisting the entity.
     *
     * <p>Automatically sets the upload timestamp.</p>
     */
    @PrePersist
    public void preData() {
        this.uploadedAt = LocalDateTime.now();
    }
}
