package com.zalopay.demo.bankprovider;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferFundController {

    @PostMapping("/transfer-fund")
    public String transferFund() {
        return "Transfer Fund Success";
    }
}
