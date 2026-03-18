package com.loandemo.Loan.API.status;

/**
 * Enumeration representing the status of an EMI (Equated Monthly Installment)
 * in the Loan Management System.
 *
 * <p>This enum is used to track the payment state of each EMI
 * associated with a loan.</p>
 *
 * <p>EMI Status Types:</p>
 * <ul>
 *     <li>{@link #PAID} - EMI has been successfully paid</li>
 *     <li>{@link #PENDING} - EMI is scheduled but not yet paid</li>
 *     <li>{@link #DUE} - EMI payment is overdue (missed payment)</li>
 * </ul>
 *
 * <p>Example Usage:</p>
 * <pre>
 * EMIStatus status = EMIStatus.PENDING;
 * </pre>
 *
 * <p>This enum ensures consistent and type-safe handling
 * of EMI payment states across the application.</p>
 *
 * @author Abhishek
 */
public enum EMIStatus {

    /**
     * EMI has been successfully paid.
     */
    PAID,

    /**
     * EMI is scheduled but not yet paid.
     */
    PENDING,

    /**
     * EMI payment is overdue.
     */
    DUE
}
