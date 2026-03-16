package com.loandemo.Loan.API;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("index")
public class Index {

    @GetMapping
    public String get() {
        return "index";
    }
}
