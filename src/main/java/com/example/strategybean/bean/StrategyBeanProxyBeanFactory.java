package com.example.strategybean.bean;

import com.example.strategybean.annotation.StrategyBean;
import com.example.strategybean.exception.NotImplementedTargetClassException;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class StrategyBeanProxyBeanFactory<T> implements FactoryBean<T>, ApplicationContextAware {
    private final Class<T> beanClassName;

    private ApplicationContext applicationContext;

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        StrategyBean annotation = AnnotationUtils.findAnnotation(beanClassName, StrategyBean.class);
        addBeanTargets(annotation);

        StrategyBeanProxy<?> strategyBeanProxy = new StrategyBeanProxy<>(beanClassName);

        return (T)Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[] {this.beanClassName},
            strategyBeanProxy);
    }

    private void addBeanTargets(StrategyBean annotation) {
        if (annotation == null) {
            throw new BeanDefinitionValidationException("No @StrategyBean found on " + beanClassName);
        }

        StrategyBean.Mapping[] mappingTables = annotation.value();
        if (mappingTables.length == 0) {
            throw new BeanDefinitionValidationException("Cannot resolve key! Define @StrategyBean.Mapping");
        }

        for (StrategyBean.Mapping mapping : mappingTables) {
            validateInterfaceImplementation(mapping.targetClass());
        }

        Map<String, T> targetMap = Arrays.stream(mappingTables)
            .collect(Collectors.toMap(StrategyBean.Mapping::mappingKey, this::getBean));
        StrategyBeanContext.strategyBeanTargets.put(beanClassName, targetMap);

        StrategyBean.Otherwise otherwise = annotation.orElse();
        validateInterfaceImplementation(otherwise.targetClass());

        T orElseTarget = getBean(otherwise);
        if (orElseTarget != null) {
            StrategyBeanContext.orElseTargets.put(beanClassName, orElseTarget);
        }
    }

    @SuppressWarnings("unchecked")
    private T getBean(StrategyBean.Mapping mappingTable) {
        if (mappingTable.targetClass() == Object.class) {
            throw new BeanDefinitionValidationException("targetClass must be set for mappingKey: " + mappingTable.mappingKey());
        }
        return (T)applicationContext.getBean(mappingTable.targetClass());
    }

    @SuppressWarnings("unchecked")
    private T getBean(StrategyBean.Otherwise otherwise) {
        if (otherwise.targetClass() == Object.class) {
            return null;
        }
        return (T)applicationContext.getBean(otherwise.targetClass());
    }

    @Override
    public Class<?> getObjectType() {
        return this.beanClassName;
    }

    private void validateInterfaceImplementation(Class<?> targetClass) {
        if (targetClass == Object.class) {
            return;
        }

        if (beanClassName.isAssignableFrom(targetClass)) {
            return;
        }

        // targetClass로 지정한 것이 StrategyBean을 어노테이션이 붙은 인터페이스를 구현하고 있지 않다면 에러
        throw new NotImplementedTargetClassException(
            String.format("%s does not implement interface %s",
                targetClass.getSimpleName(),
                beanClassName.getSimpleName())
        );
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
