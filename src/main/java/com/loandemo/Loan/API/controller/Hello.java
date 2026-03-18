package com.loandemo.Loan.API.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for testing API.
 *
 * <p>This controller provide:
 * <ul>
 *     <li>GET request which return response of String</li>
 * </ul>
 *
 * <p>All endpoint not required JWT Authentication.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(name = "Hello Test APIs")
@RestController
@RequestMapping("hello")
public class Hello {

    /**
     * For testing purpose
     * @since 1.0
     * @return String as response
     */
    @Operation(summary = "Hello", description = "For testing api auth")
    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }
}
