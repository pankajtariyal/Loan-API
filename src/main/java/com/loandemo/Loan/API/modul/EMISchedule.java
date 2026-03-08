package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.EMIStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "loan_emi_schedule")
@AllArgsConstructor
@NoArgsConstructor
public class EMISchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;
    @Column(name = "emi_number")
    private int emiNumber;
    @Column(name = "emi_date")
    private LocalDate emiDate;
    @Column(name = "principle_amount")
    private double principleAmount;
    @Column(name = "interest_amount")
    private double interestAmount;
    @Column(name = "emi_amount")
    private double emiAmount;
    @Column(name = "remaining_balance")
    private double remainingBalance;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EMIStatus status;

    @PrePersist
    public void preLoadData(){
        this.status = EMIStatus.PENDING;
    }

    @Override
    public String toString() {
        return "EMISchedule{" +
                "id=" + id +
                ", loan=" + loan.getId() +
                ", emiNumber='" + emiNumber + '\'' +
                ", emiDate=" + emiDate +
                ", principleAmount=" + principleAmount +
                ", interestAmount=" + interestAmount +
                ", emiAmount=" + emiAmount +
                ", remainingBalance=" + remainingBalance +
                ", status=" + status +
                '}';
    }
}
