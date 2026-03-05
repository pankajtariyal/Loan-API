package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.dto.document.LoanWithDocument;
import com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse;
import com.loandemo.Loan.API.modul.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("SELECT count(l) FROM Loan l WHERE l.user.id = :id")
    int countLoanByUserId(@Param("id") Long id);

    @Query("SELECT new com.loandemo.Loan.API.dto.loan.apply.LoanApplyResponse( " +
            "l.id,l.amount,l.interestRate,l.tenureMonths,l.status,l.createdAt)" +
            " FROM Loan l WHERE l.user.id = :id")
    List<LoanApplyResponse> findAllByUserId(@Param("id") Long id);

    @Query("SELECT DISTINCT l FROM Loan l LEFT JOIN FETCH l.documentList")
    List<Loan> findAllLoanWithDocument();
}
