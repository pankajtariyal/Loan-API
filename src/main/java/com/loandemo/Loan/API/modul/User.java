package com.loandemo.Loan.API.modul;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

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
    private Long user_id;
    @Column(name = "username",unique = true,nullable = false,length = 20)
    private String username;
    @Column(name = "email",unique = true,nullable = false,length = 100)
    private String email;
    @Column(name = "password",unique = false,nullable = false)
    private String password;
    @Column(name = "role",nullable = false,length = 10)
    private String role;
    @Column(name = "is_active",length = 10,nullable = false)
    private boolean isActive;

    private Timestamp created_at;

    @Column(name = "created_by",length = 20)
    private String created_by;

    private Timestamp updated_at;
    @Column(name = "updated_by",length = 20)
    private String updated_by;
    private boolean verification;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", created_at=" + created_at +
                ", created_by='" + created_by + '\'' +
                ", updated_at=" + updated_at +
                ", updated_by='" + updated_by + '\'' +
                ", verification=" + verification +
                '}';
    }
}
