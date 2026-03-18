package com.loandemo.Loan.API.status;

/**
 * Enumeration representing the verification status of documents
 * in the Loan Management System.
 *
 * <p>This enum is used to track the lifecycle of documents submitted
 * by users during the loan application process.</p>
 *
 * <p>Document Verification Stages:</p>
 * <ul>
 *     <li>{@link #PENDING} - Document has been uploaded but not yet reviewed</li>
 *     <li>{@link #UNDER_REVIEW} - Document is currently being verified</li>
 *     <li>{@link #VERIFIED} - Document has been successfully verified</li>
 *     <li>{@link #REJECTED} - Document has been rejected due to invalid or incomplete information</li>
 * </ul>
 *
 * <p>Typical Flow:</p>
 * <pre>
 * PENDING → UNDER_REVIEW → VERIFIED
 *                     → REJECTED
 * </pre>
 *
 * <p>Example Usage:</p>
 * <pre>
 * DocumentStatus status = DocumentStatus.UNDER_REVIEW;
 * </pre>
 *
 * <p>This enum ensures consistency and type safety
 * when handling document verification statuses.</p>
 *
 * @author Abhishek
 */
public enum DocumentStatus {

    /**
     * Document has been uploaded but not yet reviewed.
     */
    PENDING,

    /**
     * Document is currently under verification.
     */
    UNDER_REVIEW,

    /**
     * Document has been successfully verified.
     */
    VERIFIED,

    /**
     * Document has been rejected due to invalid or incomplete details.
     */
    REJECTED
}
