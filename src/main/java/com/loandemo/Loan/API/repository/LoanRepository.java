package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.dto.document.LoanWithDocument;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.dto.loan.get.GetAllLoan;
import com.loandemo.Loan.API.dto.loan.get.GetLoanByIdResponse;
import com.loandemo.Loan.API.modul.Loan;
import com.loandemo.Loan.API.status.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("SELECT count(l) FROM Loan l WHERE l.user.id = :id and l.status <> :status")
    int countLoanByUserId(@Param("id") Long id, @Param("status")LoanStatus status);

    @Query("SELECT new com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse( " +
            "l.id,l.amount,l.interestRate,l.tenureMonths,l.status,l.createdAt)" +
            " FROM Loan l WHERE l.user.id = :id")
    List<LoanApplyResponse> findAllByUserId(@Param("id") Long id);

    @Query("SELECT DISTINCT l FROM Loan l LEFT JOIN FETCH l.documentList")
    List<Loan> findAllLoanWithDocument();

    @Query("SELECT new com.loandemo.Loan.API.dto.loan.get.GetAllLoan(" +
            "l.id,l.amount,l.interestRate,l.tenureMonths,l.panNumber,l.status,l.rejected_reason,l.approvedBy,l.approvedAt,l.createdAt) FROM Loan l")
    List<GetAllLoan> findAllLoan();

    Optional<Loan> findByIdAndUserUserId(Long id,Long userId);
}
