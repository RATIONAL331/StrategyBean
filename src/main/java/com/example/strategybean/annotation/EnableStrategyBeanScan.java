package com.example.strategybean.annotation;

import com.example.strategybean.configuration.StrategyDefinitionRegistrar;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(StrategyDefinitionRegistrar.class)
public @interface EnableStrategyBeanScan {
    String[] basePackages();
}
