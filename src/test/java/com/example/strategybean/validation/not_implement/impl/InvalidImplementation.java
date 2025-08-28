package com.example.strategybean.validation.not_implement.impl;

import org.springframework.stereotype.Component;

@Component
public class InvalidImplementation { // StrategyBean에 선언된 인터페이스를 구현하고 있지 않음
    public String process(String type) {
        return "Invalid implementation processed: " + type;
    }
}
