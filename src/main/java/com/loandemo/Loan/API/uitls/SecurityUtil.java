package com.loandemo.Loan.API.uitls;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static String getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            System.out.println(authentication.getName());
            return ((UserDetails)authentication.getPrincipal()).getUsername();
        }
            return "anonymous";
    }
}
