package com.loandemo.Loan.API.uitls;


import java.util.UUID;

public class EmailUtil {

    public static String generateEmailVerificationToken(){
        return UUID.randomUUID().toString();
    }
}
