package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "loan_payment")
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;
    @JoinColumn(name = "emi_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EMISchedule emi;
    @Column(name = "amount")
    private double amount;
    @Column(name = "transaction_id",unique = true)
    private String transactionId;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @PrePersist
    public void autoLoad(){
        this.paymentDate = LocalDate.now();
    }

}
