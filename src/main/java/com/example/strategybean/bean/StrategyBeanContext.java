package com.example.strategybean.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class StrategyBeanContext {
    static Map<Class<?>, Map<String, ?>> strategyBeanTargets = new HashMap<>();
    static Map<Class<?>, Object> orElseTargets = new HashMap<>();

    @SuppressWarnings("unchecked")
    static <T> T getTargetBean(Class<T> type, String mappingKey) {
        return Optional.ofNullable(strategyBeanTargets.get(type))
            .map(map -> (T)map.get(mappingKey))
            .orElseGet(() -> (T)orElseTargets.get(type));
    }
}
