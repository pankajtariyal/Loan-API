package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.EMIStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing the EMI (Equated Monthly Installment) schedule for a loan.
 *
 * <p>Each EMI entry contains details about a single installment including:
 * principal amount, interest amount, due date, and remaining balance.</p>
 *
 * <p>This entity is generated when a loan is approved and is used to:
 * <ul>
 *     <li>Track repayment schedule</li>
 *     <li>Monitor payment status (PAID, PENDING, DUE)</li>
 *     <li>Calculate outstanding balance</li>
 * </ul>
 *
 * <p>Each {@link EMISchedule} is associated with a {@link Loan}.</p>
 *
 * @since 1.0
 */
@Entity
@Data
@Builder
@Table(name = "loan_emi_schedule")
@AllArgsConstructor
@NoArgsConstructor
public class EMISchedule {

    /**
     * Unique identifier for the EMI record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Loan associated with this EMI.
     */
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * Sequential number of the EMI (e.g., 1st EMI, 2nd EMI).
     */
    @Column(name = "emi_number")
    private int emiNumber;

    /**
     * Due date of the EMI.
     */
    @Column(name = "emi_date")
    private LocalDate emiDate;

    /**
     * Portion of EMI amount that goes toward principal repayment.
     */
    @Column(name = "principle_amount")
    private double principleAmount;

    /**
     * Portion of EMI amount that goes toward interest payment.
     */
    @Column(name = "interest_amount")
    private double interestAmount;

    /**
     * Total EMI amount (principal + interest).
     */
    @Column(name = "emi_amount")
    private double emiAmount;

    /**
     * Remaining loan balance after this EMI payment.
     */
    @Column(name = "remaining_balance")
    private double remainingBalance;

    /**
     * Current status of the EMI.
     *
     * <p>Possible values:</p>
     * <ul>
     *     <li>{@link EMIStatus#PENDING}</li>
     *     <li>{@link EMIStatus#PAID}</li>
     *     <li>{@link EMIStatus#DUE}</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EMIStatus status;

    /**
     * Lifecycle callback executed before persisting the entity.
     *
     * <p>Sets the default EMI status to PENDING.</p>
     */
    @PrePersist
    public void preLoadData() {
        this.status = EMIStatus.PENDING;
    }

    /**
     * Returns a string representation of the EMI schedule.
     *
     * <p>Includes loan ID instead of full loan object to avoid recursion
     * and improve readability.</p>
     *
     * @return string representation of EMI schedule
     */
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
