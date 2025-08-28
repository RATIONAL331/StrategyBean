package com.example.strategybean.example.payment;

import com.example.strategybean.annotation.StrategyBean;
import com.example.strategybean.annotation.StrategyKey;
import com.example.strategybean.example.payment.impl.CardPaymentHandler;
import com.example.strategybean.example.payment.impl.CashPaymentHandler;
import com.example.strategybean.example.payment.impl.DefaultPaymentHandler;
import com.example.strategybean.example.payment.request.PayType;
import com.example.strategybean.example.payment.request.PaymentRequest;

@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "CARD", targetClass = CardPaymentHandler.class),
        @StrategyBean.Mapping(mappingKey = "CASH", targetClass = CashPaymentHandler.class)
    },
    orElse = @StrategyBean.Otherwise(targetClass = DefaultPaymentHandler.class)
)
public interface PaymentService {
    String handle(@StrategyKey PayType type);

    String handleReq(@StrategyKey("#root.type") PaymentRequest req);

    String handleReqShortSPEL(@StrategyKey("type") PaymentRequest req);

    @StrategyKey("#root[0]")
    String handleMethodLevel(PayType type);

    String handleWithString(@StrategyKey String key);
}
