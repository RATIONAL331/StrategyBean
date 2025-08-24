package com.example.strategybean.payment.impl;

import com.example.strategybean.payment.request.PayType;
import com.example.strategybean.payment.request.PaymentRequest;
import com.example.strategybean.payment.PaymentServiceWithFallback;
import org.springframework.stereotype.Component;

@Component
public class CashPaymentHandler implements PaymentServiceWithFallback {
    public String handle(PayType type) {
        return "CASH";
    }

    public String handleReq(PaymentRequest req) {
        return "CASH";
    }

	@Override
	public String handleReqShortSPEL(PaymentRequest req) {
		return "CASH";
	}

	public String handleMethodLevel(PayType type) {
        return "CASH";
    }
}
