package com.loandemo.Loan.API.status;

/**
 * Enumeration representing different modes of payment
 * available in the Loan Management System.
 *
 * <p>This enum is used to specify how a payment is made
 * while processing loan EMIs or transactions.</p>
 *
 * <p>Supported Payment Modes:</p>
 * <ul>
 *     <li>{@link #UPI} - Payment made via Unified Payments Interface</li>
 *     <li>{@link #CASH} - Payment made using cash</li>
 *     <li>{@link #BANKING} - Payment made through bank transfer (e.g., NEFT, RTGS, IMPS)</li>
 *     <li>{@link #CARD} - Payment made using debit or credit card</li>
 * </ul>
 *
 * <p>Example Usage:</p>
 * <pre>
 * PaymentMode mode = PaymentMode.UPI;
 * </pre>
 *
 * <p>This enum helps maintain consistency and avoids invalid
 * payment mode values in the system.</p>
 *
 * @author Abhishek
 */
public enum PaymentMode {

    /**
     * Payment via Unified Payments Interface (UPI).
     */
    UPI,

    /**
     * Payment via cash.
     */
    CASH,

    /**
     * Payment via bank transfer (NEFT, RTGS, IMPS, etc.).
     */
    BANKING,

    /**
     * Payment via debit or credit card.
     */
    CARD
}
