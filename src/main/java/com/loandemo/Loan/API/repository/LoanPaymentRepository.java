package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.modul.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link LoanPayment} entities.
 *
 * <p>This repository provides database operations related to loan EMI payments,
 * including fetching transaction details associated with a specific loan.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Persist loan payment records</li>
 *     <li>Fetch transaction history for a loan</li>
 * </ul>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Long> {

    /**
     * Retrieves all payment transactions for a given loan ID.
     *
     * <p>This method uses a custom JPQL query to map results directly
     * into {@link ViewTransaction} DTO, combining data from:
     * <ul>
     *     <li>{@link LoanPayment} (transaction details)</li>
     *     <li>{@link com.loandemo.Loan.API.modul.EMISchedule} (EMI details)</li>
     * </ul>
     * </p>
     *
     * <p>Fields returned in {@link ViewTransaction}:</p>
     * <ul>
     *     <li>EMI Number</li>
     *     <li>Transaction ID</li>
     *     <li>Paid Amount</li>
     *     <li>Principal Amount</li>
     *     <li>EMI Status</li>
     *     <li>Payment Mode</li>
     *     <li>Payment Date</li>
     * </ul>
     *
     * @param loanId the ID of the loan
     * @return list of {@link ViewTransaction} representing payment history
     */
    @Query("SELECT new com.loandemo.Loan.API.dto.transaction.ViewTransaction(" +
            "e.emiNumber, p.transactionId, p.amount, e.principleAmount, e.status, " +
            "p.paymentMode, p.paymentDate) " +
            "FROM LoanPayment p JOIN p.emi e " +
            "WHERE p.loan.id = :loanId")
    List<ViewTransaction> findByLoanId(@Param("loanId") Long loanId);

}
