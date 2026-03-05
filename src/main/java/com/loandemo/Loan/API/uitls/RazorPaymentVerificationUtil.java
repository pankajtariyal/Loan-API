package com.loandemo.Loan.API.uitls;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class RazorPaymentVerificationUtil {

    public String generateKey(String order_id, String payment_id,String secrete){
        try{
            String data = order_id + "|"  + payment_id;
            Mac mac = Mac.getInstance("HmaCSha256");
            SecretKeySpec spec  = new SecretKeySpec(secrete.getBytes(),"HmacSha256");
            mac.init(spec);
            byte[]dataByte = mac.doFinal(data.getBytes());
            return hexToByte(dataByte);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String hexToByte(byte[] dataByte){
        StringBuilder hexString = new StringBuilder();
        for(byte b: dataByte){
            String n = Integer.toHexString(0xff & b);
            if(n.length()==1) hexString.append("0");
            hexString.append(n);
        }
        return hexString.toString();
    }

}
