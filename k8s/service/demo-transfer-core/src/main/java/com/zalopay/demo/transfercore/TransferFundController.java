package com.zalopay.demo.transfercore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TransferFundController {

    @Value("${transfer.core.host}")
    String transferCoreHost;

    @PostMapping("/transfer-fund")
    public String transferFund() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.postForEntity(transferCoreHost + "/transfer-fund",null, String.class);
        if(response.getStatusCode() == HttpStatus.OK && "Transfer Fund Success".equals(response.getBody())) {
            return "Transfer Fund Success";
        }
        return "Transfer Fund Fail";
    }
}
