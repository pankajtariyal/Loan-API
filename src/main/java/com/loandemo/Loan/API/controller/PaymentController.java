package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.PaymentVerification;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.uitls.RazorPaymentVerificationUtil;
import com.razorpay.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("payment")
@CrossOrigin(origins = "http://localhost:3001")
public class PaymentController {

    @Autowired
    private RazorPaymentVerificationUtil razorPaymentVerificationUtil;

    @Value("${razorpay.key.id}")
    String id;
    @Value("${razorpay.key.secret}")
    String key;

//    {
//        "amount": 100,
//            "currency": "INR",
//            "receipt": "Abhishek",
//            "notes": {
//        "notes_key_1": "Testing",
//                "notes_key_2": "Creating orderusing Razorpay API."
//    }
//    }
    @PostMapping("order/{amount}")
    public String makeOrder(@PathVariable("amount")int amount) throws RazorpayException {
        System.out.println(id);
        System.out.println(key);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount",amount*100);
        jsonObject.put("currency","INR");
        jsonObject.put("receipt","Pankaj");
        RazorpayClient razorpayClient = new RazorpayClient(id,key);
        Order order = razorpayClient.orders.create(jsonObject);
        return order.toString();
    }

    @PostMapping("verify")
    public ResponseEntity<APIResponse<String>> verifyPayment(@RequestBody Map<String,String> data){
        String order_id = data.get("order_id");
        String payment_id = data.get("payment_id");
        String signature = data.get("signature");
        String check = razorPaymentVerificationUtil.generateKey(order_id,payment_id,key);
        if(check.equals(signature)){
            return ResponseEntity.ok(APIResponse.success("valid"));
        }else {
            return ResponseEntity.ok(APIResponse.error("invalid"));
        }
    }

//    @GetMapping("checkAmount/{order_id}")
//    public ResponseEntity<APIResponse<String>> checkAmt(@PathVariable String order_id) throws RazorpayException {
//        RazorpayClient razorpayClient = new RazorpayClient(id,key);
//        PaymentLinkClient paymentLinkClient  = razorpayClient.paymentLink;
//        return ResponseEntity.ok(APIResponse.success(paymentLinkClient.fetch(order_id).toString()));
//    }
}
