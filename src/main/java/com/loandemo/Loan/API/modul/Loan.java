package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a loan in the system.
 *
 * <p>A loan belongs to a user and contains information about:
 * <ul>
 *     <li>Loan amount</li>
 *     <li>Interest rate</li>
 *     <li>Tenure</li>
 *     <li>Loan status</li>
 *     <li>Loan rejection reason</li>
 *     <li>Loan create at</li>
 *     <li>Loan create by</li>
 *     <li>Loan approved at</li>
 *     <li>Loan approved by</li>
 *
 * </ul>
 *
 * @implNote Each loan may have multiple Document associated with it.
 *
 * @since 1.0
 */
@Entity
@Table(name = "loan_application")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    /**
     * Unique identifier for the loan.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who applied for the loan.
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    /**
     * Loan amount user need.
     */
    @Column(name = "amount",precision = 15,scale = 2,nullable = false)
    private double amount;

    /**
     * Loan interest rate.
     */
    @Column(name = "interest_rate",precision = 5,scale = 2,nullable = false)
    private double interestRate;

    /**
     * Loan tenure period of loan.
     */
    @Column(name = "tenure_month", length = 2)
    private int tenureMonths;

    /**
     * User pan number.
     */
    @Column(name = "pan_number",nullable = false,length = 10)
    private String panNumber;

    /**
     * Loan status.
     * <ul>
     *     <li>PENDING</li>
     *     <li>DOCUMENT_PENDING</li>
     *     <li>UNDER_REVIEW</li>
     *     <li>APPROVED</li>
     *     <li>REJECTED</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    /**
     * Loan rejection reason by admin.
     */
    private String rejected_reason;

    /**
     * Who approved loan.
     */
    @ManyToOne
    @JoinColumn(name = "approved_by",nullable = true)
    private User approvedBy;

    /**
     * Loan approved at.
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    /**
     * When loan apply.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Document need for loan to approved.
     */
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Document> documentList;

    /**
     * Loan versioning.
     */
    @Version
    private Long version;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.status = LoanStatus.DOCUMENT_PENDING;
        if(this.panNumber!=null){
            this.panNumber = this.panNumber.toUpperCase();
        }
    }
}
