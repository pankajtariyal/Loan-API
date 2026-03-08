package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.EMIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("loan/emi")
public class EMIController {

    private final EMIService emiService;

    @Autowired
    public EMIController(EMIService emiService){
        this.emiService = emiService;
    }

    @GetMapping("{id}/emi-schedule")
    public ResponseEntity<APIResponse<List<ViewEmi>>> getAllLoanEmi(@PathVariable("id")Long id){
        List<ViewEmi> list = emiService.getAllEmiByLoanId(id);
        return ResponseEntity.ok(APIResponse.success(list));
    }
}
