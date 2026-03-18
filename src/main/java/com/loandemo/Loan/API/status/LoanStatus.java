package com.loandemo.Loan.API.status;

/**
 * Enumeration representing the different states of a loan
 * in the Loan Management System.
 *
 * <p>This enum defines the lifecycle of a loan from creation
 * to closure, helping maintain consistent status handling
 * across the application.</p>
 *
 * <p>Loan Lifecycle Stages:</p>
 * <ul>
 *     <li>{@link #PENDING} - Loan application has been created but not yet processed</li>
 *     <li>{@link #DOCUMENT_PENDING} - Required documents are yet to be submitted</li>
 *     <li>{@link #UNDER_REVIEW} - Loan is under verification and evaluation</li>
 *     <li>{@link #VERIFIED} - Documents and details have been verified</li>
 *     <li>{@link #APPROVED} - Loan has been approved</li>
 *     <li>{@link #REJECTED} - Loan application has been rejected</li>
 *     <li>{@link #CLOSED} - Loan has been fully paid and closed</li>
 * </ul>
 *
 * <p>Example Usage:</p>
 * <pre>
 * LoanStatus status = LoanStatus.APPROVED;
 * </pre>
 *
 * <p>This enum ensures type safety and prevents invalid
 * loan status values.</p>
 *
 * @author Abhishek
 */
public enum LoanStatus {

    /**
     * Loan application created but not yet processed.
     */
    PENDING,

    /**
     * Required documents are pending submission.
     */
    DOCUMENT_PENDING,

    /**
     * Loan is currently under review.
     */
    UNDER_REVIEW,

    /**
     * Loan has been approved.
     */
    APPROVED,

    /**
     * Loan application has been rejected.
     */
    REJECTED,

    /**
     * Loan has been verified after document validation.
     */
    VERIFIED,

    /**
     * Loan has been fully repaid and closed.
     */
    CLOSED
}
