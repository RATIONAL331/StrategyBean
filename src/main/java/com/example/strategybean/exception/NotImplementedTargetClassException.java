package com.example.strategybean.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotImplementedTargetClassException extends RuntimeException {
    public NotImplementedTargetClassException(String message) {
        super(message);
    }
}
