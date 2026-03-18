package com.loandemo.Loan.API.status;

/**
 * Enumeration representing different types of documents
 * required in the Loan Management System.
 *
 * <p>This enum is used to categorize and validate documents
 * submitted by users during the loan application process.</p>
 *
 * <p>Supported Document Types:</p>
 * <ul>
 *     <li>{@link #PAN} - Permanent Account Number (PAN) card</li>
 *     <li>{@link #SALARY_SLIP} - Salary slip for income verification</li>
 *     <li>{@link #ID_PROOF} - Identity proof (e.g., Aadhaar, Passport, Voter ID)</li>
 * </ul>
 *
 * <p>Example Usage:</p>
 * <pre>
 * DocumentType type = DocumentType.PAN;
 * </pre>
 *
 * <p>This enum ensures type safety and prevents invalid
 * document type values in the system.</p>
 *
 * @author Abhishek
 */
public enum DocumentType {

    /**
     * Permanent Account Number (PAN) card used for identity and tax verification.
     */
    PAN,

    /**
     * Salary slip used to verify income details of the applicant.
     */
    SALARY_SLIP,

    /**
     * Identity proof such as Aadhaar, Passport, or Voter ID.
     */
    ID_PROOF
}
