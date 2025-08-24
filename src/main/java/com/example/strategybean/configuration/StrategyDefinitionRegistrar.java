package com.example.strategybean.configuration;

import com.example.strategybean.annotation.EnableStrategyBeanScan;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.example.strategybean.bean.StrategyBeanProxyBeanFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StrategyDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(
			@Nonnull AnnotationMetadata importingClassMetadata,
			@Nonnull BeanDefinitionRegistry registry
	) {
		StrategyBeanScanner routingBeanScanner = new StrategyBeanScanner(registry);

		for (String basePackage : getBasePackage(importingClassMetadata)) {
			Set<BeanDefinitionHolder> beanDefinitionHolders = routingBeanScanner.scan(basePackage);

			for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
				log.debug("register bean definition: {}", beanDefinitionHolder.getBeanName());

				GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
				String beanClassName = beanDefinition.getBeanClassName();
				if (beanClassName == null) {
					throw new RuntimeException("beanClassName must be set");
				}

				beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
				beanDefinition.setBeanClass(StrategyBeanProxyBeanFactory.class);
				beanDefinition.setPrimary(true);
			}
		}
	}

	private Set<String> getBasePackage(AnnotationMetadata metadata) {
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableStrategyBeanScan.class.getName()));
		Set<String> basePackages = Optional.ofNullable(mapperScanAttrs)
		                                   .map(attr -> Arrays.stream(attr.getStringArray("basePackages")))
		                                   .orElseGet(Stream::empty)
		                                   .filter(StringUtils::hasText)
		                                   .collect(Collectors.toSet());

		if (CollectionUtils.isEmpty(basePackages)) {
			return Collections.singleton(getDefaultBasePackage(metadata));
		}
		return basePackages;
	}

	private String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}
}
