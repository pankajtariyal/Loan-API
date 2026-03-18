package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.status.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for managing {@link Loan} entities.
 *
 * <p>This repository provides CRUD operations and custom queries
 * for handling loan-related data, including fetching loan summaries,
 * counting loans, and retrieving loan details along with associated documents.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Count active loans for a user</li>
 *     <li>Fetch loan applications by user</li>
 *     <li>Retrieve loans with associated documents</li>
 *     <li>Fetch all loans in DTO format</li>
 *     <li>Validate loan ownership</li>
 * </ul>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Counts the number of loans for a given user excluding a specific status.
     *
     * <p>Typically used to check active loans by excluding CLOSED or REJECTED loans.</p>
     *
     * @param id user ID
     * @param status loan status to exclude
     * @return count of loans
     */
    @Query("SELECT count(l) FROM Loan l WHERE l.user.id = :id and l.status <> :status")
    int countLoanByUserId(@Param("id") Long id, @Param("status")LoanStatus status);

    /**
     * Retrieves all loans for a given user and maps them
     * to {@link LoanApplyResponse} DTO.
     *
     * <p>Used to show loan application summary to users.</p>
     *
     * @param id user ID
     * @return list of {@link LoanApplyResponse}
     */
    @Query("SELECT new com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse( " +
            "l.id,l.amount,l.interestRate,l.tenureMonths,l.status,l.createdAt)" +
            " FROM Loan l WHERE l.user.id = :id")
    List<LoanApplyResponse> findAllByUserId(@Param("id") Long id);

    /**
     * Fetches all loans along with their associated documents.
     *
     * <p>This method uses {@code LEFT JOIN FETCH} to eagerly load
     * the document list and avoid LazyInitializationException.</p>
     *
     * @return list of {@link Loan} with documents
     */
    @Query("SELECT DISTINCT l FROM Loan l LEFT JOIN FETCH l.documentList")
    List<Loan> findAllLoanWithDocument();

    /**
     * Retrieves all loans and maps them into {@link GetAllLoan} DTO.
     *
     * <p>Includes detailed loan information such as:</p>
     * <ul>
     *     <li>Loan ID</li>
     *     <li>Amount</li>
     *     <li>Interest Rate</li>
     *     <li>Tenure</li>
     *     <li>PAN Number</li>
     *     <li>Status</li>
     *     <li>Approval/Rejection details</li>
     *     <li>Creation timestamp</li>
     * </ul>
     *
     * @return list of {@link GetAllLoan}
     */
    @Query("SELECT new com.loandemo.Loan.API.dto.loan.get.GetAllLoan(" +
            "l.id,l.amount,l.interestRate,l.tenureMonths,l.panNumber,l.status,l.rejected_reason,l.approvedAt,l.createdAt) FROM Loan l")
    List<GetAllLoan> findAllLoan();

    /**
     * Finds a loan by its ID and ensures it belongs to the given user.
     *
     * <p>This method is used for security validation to ensure
     * that a user can only access their own loan.</p>
     *
     * @param id loan ID
     * @param userId user ID
     * @return {@link Optional} containing loan if found
     */
    Optional<Loan> findByIdAndUserUserId(Long id,Long userId);
}
