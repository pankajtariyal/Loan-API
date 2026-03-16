package com.loandemo.Loan.API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Hello Test APIs")
@RestController
@RequestMapping("hello")
public class Hello {

    @Operation(summary = "Hello", description = "For testing api auth")
    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }
}
