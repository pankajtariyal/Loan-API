package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.EMISchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EMIRepository extends JpaRepository<EMISchedule,Long> {
}
