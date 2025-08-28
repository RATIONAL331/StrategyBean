package com.example.strategybean.validation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.strategybean.annotation.EnableStrategyBeanScan;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.strategybean.validation")
@EnableStrategyBeanScan(basePackages = "com.example.strategybean.validation")
public class ValidationStrategyBeanTestConfig {
}
