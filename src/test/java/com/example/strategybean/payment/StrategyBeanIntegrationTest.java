package com.example.strategybean.payment;

import com.example.strategybean.StrategyBeanTestConfig;
import com.example.strategybean.payment.request.PayType;
import com.example.strategybean.payment.request.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = StrategyBeanTestConfig.class)
class StrategyBeanIntegrationTest {

    @Autowired
    private PaymentServiceWithFallback withFallback;

    @Test
    @DisplayName("Enum parameter with @StrategyKey on param routes correctly and uses orElse for unsupported")
    void enumParamRouting_withFallback_andDefault() {
        assertEquals("CARD", withFallback.handle(PayType.CARD));
        assertEquals("CASH", withFallback.handle(PayType.CASH));
        assertEquals("DEFAULT", withFallback.handle(PayType.POINT));
    }

    @Test
    @DisplayName("Method-level @StrategyKey with #root[0] routes correctly")
    void methodLevelRouting() {
        assertEquals("CARD", withFallback.handleMethodLevel(PayType.CARD));
        assertEquals("CASH", withFallback.handleMethodLevel(PayType.CASH));
        assertEquals("DEFAULT", withFallback.handleMethodLevel(PayType.POINT));
    }

    @Test
    @DisplayName("POJO parameter with @StrategyKey SpEL '#root.type' routes correctly and uses orElse")
    void pojoParamSpELRouting_withFallback() {
        assertEquals("CARD", withFallback.handleReq(new PaymentRequest(PayType.CARD)));
        assertEquals("CASH", withFallback.handleReq(new PaymentRequest(PayType.CASH)));
        assertEquals("DEFAULT", withFallback.handleReq(new PaymentRequest(PayType.POINT)));
    }

	@Test
	@DisplayName("POJO parameter with @StrategyKey short SpEL 'type' routes correctly and uses orElse")
	void pojoParamShortSpELRouting_withFallback() {
		assertEquals("CARD", withFallback.handleReqShortSPEL(new PaymentRequest(PayType.CARD)));
		assertEquals("CASH", withFallback.handleReqShortSPEL(new PaymentRequest(PayType.CASH)));
		assertEquals("DEFAULT", withFallback.handleReqShortSPEL(new PaymentRequest(PayType.POINT)));
	}
}
