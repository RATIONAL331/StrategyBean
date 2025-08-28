package com.example.strategybean.example.payment;

import com.example.strategybean.example.ExampleStrategyBeanTestConfig;
import com.example.strategybean.example.payment.request.PayType;
import com.example.strategybean.example.payment.request.PaymentRequest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ExampleStrategyBeanTestConfig.class)
class PaymentServiceStrategyBeanIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("Enum parameter with @StrategyKey on param routes correctly and uses orElse for unsupported")
    void enumParamRouting_withFallback_andDefault() {
        Assertions.assertThat(paymentService.handle(PayType.CARD)).isEqualTo("CARD");
        Assertions.assertThat(paymentService.handle(PayType.CASH)).isEqualTo("CASH");
        Assertions.assertThat(paymentService.handle(PayType.POINT)).isEqualTo("DEFAULT");
    }

    @Test
    @DisplayName("Method-level @StrategyKey with #root[0] routes correctly")
    void methodLevelRouting() {
        Assertions.assertThat(paymentService.handleMethodLevel(PayType.CARD)).isEqualTo("CARD");
        Assertions.assertThat(paymentService.handleMethodLevel(PayType.CASH)).isEqualTo("CASH");
        Assertions.assertThat(paymentService.handleMethodLevel(PayType.POINT)).isEqualTo("DEFAULT");
    }

    @Test
    @DisplayName("POJO parameter with @StrategyKey SpEL '#root.type' routes correctly and uses orElse")
    void pojoParamSpELRouting_withFallback() {
        Assertions.assertThat(paymentService.handleReq(new PaymentRequest(PayType.CARD))).isEqualTo("CARD");
        Assertions.assertThat(paymentService.handleReq(new PaymentRequest(PayType.CASH))).isEqualTo("CASH");
        Assertions.assertThat(paymentService.handleReq(new PaymentRequest(PayType.POINT))).isEqualTo("DEFAULT");
    }

    @Test
    @DisplayName("POJO parameter with @StrategyKey short SpEL 'type' routes correctly and uses orElse")
    void pojoParamShortSpELRouting_withFallback() {
        Assertions.assertThat(paymentService.handleReqShortSPEL(new PaymentRequest(PayType.CARD))).isEqualTo("CARD");
        Assertions.assertThat(paymentService.handleReqShortSPEL(new PaymentRequest(PayType.CASH))).isEqualTo("CASH");
        Assertions.assertThat(paymentService.handleReqShortSPEL(new PaymentRequest(PayType.POINT))).isEqualTo("DEFAULT");
    }

    @Test
    @DisplayName("String parameter with @StrategyKey")
    void stringParamRouting_withFallback() {
        Assertions.assertThat(paymentService.handleWithString("CARD")).isEqualTo("CARD");
        Assertions.assertThat(paymentService.handleWithString("CASH")).isEqualTo("CASH");
        Assertions.assertThat(paymentService.handleWithString("POINT")).isEqualTo("DEFAULT");
    }
}
