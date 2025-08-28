package com.example.strategybean.example.payment.impl;

import com.example.strategybean.example.payment.request.PayType;
import com.example.strategybean.example.payment.request.PaymentRequest;
import com.example.strategybean.example.payment.PaymentService;

import org.springframework.stereotype.Component;

@Component
public class DefaultPaymentHandler implements PaymentService {
    @Override
    public String handle(PayType type) {
        return "DEFAULT";
    }

    @Override
    public String handleReq(PaymentRequest req) {
        return "DEFAULT";
    }

    @Override
    public String handleReqShortSPEL(PaymentRequest req) {
        return "DEFAULT";
    }

    @Override
    public String handleMethodLevel(PayType type) {
        return "DEFAULT";
    }

    @Override
    public String handleWithString(String key) {
        return "DEFAULT";
    }
}
