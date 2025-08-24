package com.example.strategybean.payment.impl;

import com.example.strategybean.payment.request.PayType;
import com.example.strategybean.payment.request.PaymentRequest;
import com.example.strategybean.payment.PaymentServiceWithFallback;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentHandler implements PaymentServiceWithFallback {
    public String handle(PayType type) {
        return "CARD";
    }

    public String handleReq(PaymentRequest req) {
        return "CARD";
    }

	@Override
	public String handleReqShortSPEL(PaymentRequest req) {
		return "CARD";
	}

	public String handleMethodLevel(PayType type) {
        return "CARD";
    }
}
