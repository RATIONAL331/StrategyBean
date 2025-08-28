package com.example.strategybean.validation.not_implement;

import com.example.strategybean.annotation.StrategyBean;
import com.example.strategybean.annotation.StrategyKey;
import com.example.strategybean.validation.not_implement.impl.ValidImplementation;
import com.example.strategybean.validation.not_implement.impl.InvalidImplementation;

@StrategyBean(
    value = {
        @StrategyBean.Mapping(mappingKey = "VALID", targetClass = ValidImplementation.class),
        @StrategyBean.Mapping(mappingKey = "INVALID", targetClass = InvalidImplementation.class) // InvalidImplementation은 ValidationService를 구현하지 않음
    }
)
public interface ValidationService {
    String process(@StrategyKey String type);
}
