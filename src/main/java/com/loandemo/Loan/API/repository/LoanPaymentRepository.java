package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.dto.transaction.ViewTransaction;
import com.loandemo.Loan.API.modul.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment,Long> {
    @Query("SELECT new com.loandemo.Loan.API.dto.transaction.ViewTransaction(" +
            "e.emiNumber, p.transactionId, p.amount, e.principleAmount, e.status, " +
            "p.paymentMode, p.paymentDate) " +
            "FROM LoanPayment p JOIN p.emi e " +
            "WHERE p.loan.id = :loanId")
    List<ViewTransaction> findByLoanId(@Param("loanId") Long loanId);

}
