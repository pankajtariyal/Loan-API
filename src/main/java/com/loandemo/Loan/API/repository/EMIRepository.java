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

@Repository
public interface EMIRepository extends JpaRepository<EMISchedule,Long> {

    List<EMISchedule> findByLoanIdOrderByEmiNumber(Long id);

    @Query("SELECT e FROM EMISchedule e WHERE e.id = :emiId AND e.loan.id = :loanId")
    Optional<EMISchedule> findByIdAndLoanId(@Param("emiId") Long emiId, @Param("loanId") Long loanId);

    @Query("SELECT COUNT(e) FROM EMISchedule e WHERE e.loan.id=:loanId and e.status=:status")
    int countEmiStatus(@Param("loanId") Long loanId, @Param("status")EMIStatus status);
}
