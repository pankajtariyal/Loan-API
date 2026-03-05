package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.DocumentStatus;
import com.loandemo.Loan.API.status.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_document", uniqueConstraints = {
@UniqueConstraint(columnNames = {"loan_id", "document_type"})
    })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type",length = 255,nullable = false)
    private DocumentType documentType;
    @Column(name = "file_path",length = 255,nullable = false)
    private String filePath;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private DocumentStatus status;
    @Column(name = "rejected_reason",nullable = true)
    private String rejectedReason;
    @Column(name = "uploaded_at",nullable = false)
    private LocalDateTime uploadedAt;
    @Version
    private int version;

    @PrePersist
    public void preData(){
        this.uploadedAt = LocalDateTime.now();
    }
}
