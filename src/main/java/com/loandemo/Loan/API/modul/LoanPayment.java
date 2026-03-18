package com.loandemo.Loan.API.modul;

import com.loandemo.Loan.API.status.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing a payment made towards a loan EMI.
 *
 * <p>This entity stores details of each payment transaction made by a user
 * for a specific {@link Loan} and its corresponding {@link EMISchedule}.</p>
 *
 * <p>Each payment includes information such as:</p>
 * <ul>
 *     <li>Payment amount</li>
 *     <li>Transaction ID (unique for each payment)</li>
 *     <li>Payment mode (e.g., UPI, CARD, NET_BANKING)</li>
 *     <li>Payment date</li>
 * </ul>
 *
 * <p>This entity is useful for:</p>
 * <ul>
 *     <li>Tracking EMI payments</li>
 *     <li>Maintaining transaction history</li>
 *     <li>Generating payment reports</li>
 * </ul>
 *
 * @since 1.0
 */
@Entity
@Data
@Builder
@Table(name = "loan_payment")
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayment {

    /**
     * Unique identifier for the loan payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Loan associated with this payment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * EMI associated with this payment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id", nullable = false)
    private EMISchedule emi;

    /**
     * Amount paid for the EMI.
     */
    @Column(name = "amount")
    private double amount;

    /**
     * Unique transaction ID for the payment.
     *
     * <p>This is typically generated using UUID to ensure uniqueness.</p>
     */
    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    /**
     * Mode of payment used for the transaction.
     *
     * <p>Possible values:</p>
     * <ul>
     *     <li>{@link PaymentMode}</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    /**
     * Date on which the payment was made.
     */
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    /**
     * Lifecycle callback executed before persisting the entity.
     *
     * <p>Automatically sets the payment date to the current date.</p>
     */
    @PrePersist
    public void autoLoad() {
        this.paymentDate = LocalDate.now();
    }
}
