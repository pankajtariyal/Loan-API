package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loan_application")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "amount",precision = 15,scale = 2,nullable = false)
    private double amount;
    @Column(name = "interest_rate",precision = 5,scale = 2,nullable = false)
    private double interestRate;
    @Column(name = "tenure_month", length = 2)
    private int tenureMonths;
    @Column(name = "pan_number",nullable = false,length = 10)
    private String panNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;
    private String rejected_reason;

    @ManyToOne
    @JoinColumn(name = "approved_by",nullable = true)
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Document> documentList;

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
