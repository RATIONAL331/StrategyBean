package com.example.strategybean.example.payment.request;

public class PaymentRequest {
    private PayType type;

    public PaymentRequest() {}
    public PaymentRequest(PayType type) {
        this.type = type;
    }

    public PayType getType() {
        return type;
    }

    public void setType(PayType type) {
        this.type = type;
    }
}
