package com.example.strategybean.validation.not_implement;

import com.example.strategybean.exception.NotImplementedTargetClassException;
import com.example.strategybean.validation.ValidationStrategyBeanTestConfig;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = ValidationStrategyBeanTestConfig.class)
class ValidationServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("InvalidImplementation으로 인한 프록시 생성 실패 테스트")
    void testProxyCreationFailure() {
        Assertions.assertThatThrownBy(() -> applicationContext.getBean(ValidationService.class))
            .isInstanceOf(BeanCreationException.class)
            .rootCause()
            .isInstanceOf(NotImplementedTargetClassException.class)
            .hasMessageContaining("InvalidImplementation does not implement interface ValidationService");
    }
}
