package com.loandemo.Loan.API.modul;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Table(name = "user_master")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger user_id;
    private String username;
    private String email;
    private String password_hash;
    private String role;
    private boolean isActive;
    private Timestamp created_at;
    private String created_by;
    private Timestamp updated_at;
    private String updated_by;
}
