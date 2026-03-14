package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiRequest;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.EMIService;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("{loanId}/eminumber/{emiId}")
    public ResponseEntity<APIResponse<PayEmiResponse>> payEmiByIds(@PathVariable("emiId")Long emiId,
                                                                   @PathVariable("loanId")Long loanId,
                                                                   @RequestBody PayEmiRequest emiRequest) throws Exception {
        PayEmiResponse response = emiService.payEmi(loanId,emiId,emiRequest);
        return ResponseEntity.ok(APIResponse.success(response));
    }
}
