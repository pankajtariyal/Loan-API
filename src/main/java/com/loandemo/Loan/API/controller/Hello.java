package com.loandemo.Loan.API.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class Hello {

    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }
}
