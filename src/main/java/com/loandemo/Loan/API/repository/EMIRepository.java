package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.EMISchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EMIRepository extends JpaRepository<EMISchedule,Long> {

    List<EMISchedule> findByLoanIdOrderByEmiNumber(Long id);

}
