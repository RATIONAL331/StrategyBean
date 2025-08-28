package com.example.strategybean.example.payment.impl;

import com.example.strategybean.example.payment.request.PayType;
import com.example.strategybean.example.payment.request.PaymentRequest;
import com.example.strategybean.example.payment.PaymentService;

import org.springframework.stereotype.Component;

@Component
public class CashPaymentHandler implements PaymentService {
    @Override
    public String handle(PayType type) {
        return "CASH";
    }

    @Override
    public String handleReq(PaymentRequest req) {
        return "CASH";
    }

    @Override
    public String handleReqShortSPEL(PaymentRequest req) {
        return "CASH";
    }

    @Override
    public String handleMethodLevel(PayType type) {
        return "CASH";
    }

    @Override
    public String handleWithString(String key) {
        return "CASH";
    }
}
