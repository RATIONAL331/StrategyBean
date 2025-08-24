package com.example.strategybean;

import com.example.strategybean.annotation.EnableStrategyBeanScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.example.strategybean")
@EnableStrategyBeanScan(basePackages = "com.example.strategybean")
public class StrategyBeanTestConfig {
}
