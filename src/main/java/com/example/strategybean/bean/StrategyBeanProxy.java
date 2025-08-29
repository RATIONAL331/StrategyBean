package com.example.strategybean.bean;

import com.example.strategybean.exception.UnSupportedStrategyKeyException;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@RequiredArgsConstructor
public class StrategyBeanProxy<T> implements InvocationHandler {
    private static final Map<Method, StrategyKeyContext> strategyKeyContexts = new HashMap<>();

    private final Class<T> beanClass;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StrategyKeyContext strategyKeyContext = getStrategyKeyContext(method);
        String mappingKey = strategyKeyContext.getExpressionValue(args);
        T targetBean = StrategyBeanContext.getTargetBean(beanClass, mappingKey);
        if (targetBean == null) {
            throw new UnSupportedStrategyKeyException(String.format("No target bean found for key [%s]", mappingKey));
        }
        return invokeMethod(targetBean, method, args);
    }

    private StrategyKeyContext getStrategyKeyContext(Method method) {
        return strategyKeyContexts.computeIfAbsent(method, StrategyKeyContext::new);
    }

    private Object invokeMethod(T targetBean, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(targetBean, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
