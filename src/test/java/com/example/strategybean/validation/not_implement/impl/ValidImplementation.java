package com.example.strategybean.validation.not_implement.impl;

import com.example.strategybean.validation.not_implement.ValidationService;

import org.springframework.stereotype.Component;

@Component
public class ValidImplementation implements ValidationService {
    @Override
    public String process(String type) {
        return "Valid implementation processed: " + type;
    }
}
