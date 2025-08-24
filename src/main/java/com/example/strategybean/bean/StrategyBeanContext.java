package com.example.strategybean.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class StrategyBeanContext {
	static Map<Class<?>, Map<String, ?>> strategyBeanTargets = new HashMap<>();
	static Map<Class<?>, Object> orElseTargets = new HashMap<>();

	static <T> T getTargetBean(Class<T> type, String routingKey) {
		return Optional.ofNullable(strategyBeanTargets.get(type))
		               .map(map -> (T) map.get(routingKey))
		               .orElseGet(() -> (T) orElseTargets.get(type));
	}
}
