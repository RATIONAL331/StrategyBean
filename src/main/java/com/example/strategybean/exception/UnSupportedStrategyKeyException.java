package com.example.strategybean.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnSupportedStrategyKeyException extends RuntimeException {
    public UnSupportedStrategyKeyException(String message) {
        super(message);
    }
}
