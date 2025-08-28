package com.example.strategybean.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnSupportedStrategyKeyException extends RuntimeException {
    private final String routingKey;
}
