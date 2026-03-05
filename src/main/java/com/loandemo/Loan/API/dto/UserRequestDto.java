package com.loandemo.Loan.API.modul;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String email;
    private String role;
    private String password;
    private boolean is_active;
    private String created_by;
    private String updated_by;
}
