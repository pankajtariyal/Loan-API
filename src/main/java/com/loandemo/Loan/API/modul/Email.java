package com.loandemo.Loan.API.modul;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "email")
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token", length = 255,unique = true,nullable = false)
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    private boolean used;
}
