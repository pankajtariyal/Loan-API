package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.EMISchedule;
import com.loandemo.Loan.API.status.EMIStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link EMISchedule} entities.
 *
 * <p>Provides CRUD operations and custom query methods for handling
 * EMI (Equated Monthly Installment) data.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Fetch EMI schedules for a loan</li>
 *     <li>Retrieve specific EMI by loan and EMI ID</li>
 *     <li>Count EMIs based on their status</li>
 * </ul>
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Repository
public interface EMIRepository extends JpaRepository<EMISchedule, Long> {

    /**
     * Retrieves all EMI schedules for a given loan ID,
     * ordered by EMI number in ascending order.
     *
     * @param id loan ID
     * @return list of {@link EMISchedule}
     */
    List<EMISchedule> findByLoanIdOrderByEmiNumber(Long id);


    /**
     * Finds a specific EMI by its ID and associated loan ID.
     *
     * <p>This ensures that the EMI belongs to the given loan,
     * providing additional validation/security.</p>
     *
     * @param emiId EMI ID
     * @param loanId loan ID
     * @return {@link Optional} containing EMI if found
     */
    @Query("SELECT e FROM EMISchedule e WHERE e.id = :emiId AND e.loan.id = :loanId")
    Optional<EMISchedule> findByIdAndLoanId(@Param("emiId") Long emiId, @Param("loanId") Long loanId);

    /**
     * Counts the number of EMIs for a given loan ID
     * with a specific status.
     *
     * <p>Used to track payment progress, such as:
     * <ul>
     *     <li>Pending EMIs</li>
     *     <li>Paid EMIs</li>
     * </ul>
     * </p>
     *
     * @param loanId loan ID
     * @param status EMI status ({@link EMIStatus})
     * @return count of EMIs matching the status
     */
    @Query("SELECT COUNT(e) FROM EMISchedule e WHERE e.loan.id=:loanId and e.status=:status")
    int countEmiStatus(@Param("loanId") Long loanId, @Param("status")EMIStatus status);
}
