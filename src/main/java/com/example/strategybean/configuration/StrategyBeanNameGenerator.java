package com.example.strategybean.configuration;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

class StrategyBeanNameGenerator extends AnnotationBeanNameGenerator {
	private static final String PREFIX = "StrategyBean-";

	@Nonnull
	@Override
	public String generateBeanName(
			@Nonnull BeanDefinition definition,
			@Nonnull BeanDefinitionRegistry registry
	) {
		return PREFIX + super.generateBeanName(definition, registry);
	}

	public static boolean isRoutingBeanName(String beanName) {
		return beanName.startsWith(PREFIX);
	}
}
