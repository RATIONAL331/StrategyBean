package com.example.strategybean.example;

import com.example.strategybean.annotation.EnableStrategyBeanScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.strategybean.example")
@EnableStrategyBeanScan(basePackages = "com.example.strategybean.example")
public class ExampleStrategyBeanTestConfig {
}
