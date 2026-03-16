package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.emiDto.ViewEmi;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiRequest;
import com.loandemo.Loan.API.dto.emiDto.pay.PayEmiResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.EMIService;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@Tag(
        name="EMI APIs",
        description = "Operation related to EMI")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("loan/emi")
public class EMIController {

    private final EMIService emiService;

    @Autowired
    public EMIController(EMIService emiService){
        this.emiService = emiService;
    }

    @Operation(summary = "Get All EMI", description = "Customer will get all emi of their loan by laon id")
    @GetMapping("{id}/emi-schedule")
    public ResponseEntity<APIResponse<List<ViewEmi>>> getAllLoanEmi(
            @Parameter(description = "Loan Id") @PathVariable("id")Long id){
        List<ViewEmi> list = emiService.getAllEmiByLoanId(id);
        return ResponseEntity.ok(APIResponse.success(list));
    }

    @Operation(summary = "Pay EMI", description = "Customer to pay their loan EMI")
    @PostMapping("{loanId}/eminumber/{emiId}")
    public ResponseEntity<APIResponse<PayEmiResponse>> payEmiByIds(
            @Parameter(description = "EMI Id") @PathVariable("emiId")Long emiId,
            @Parameter(description = "Loan Id") @PathVariable("loanId")Long loanId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "EMI Pay Modal",
                    required = true
            ) @RequestBody PayEmiRequest emiRequest) throws Exception {
        PayEmiResponse response = emiService.payEmi(loanId,emiId,emiRequest);
        return ResponseEntity.ok(APIResponse.success(response));
    }
}
