package com.loandemo.Loan.API.repository;

import com.loandemo.Loan.API.modul.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {
    Optional<Email> findByToken(String token);
}
