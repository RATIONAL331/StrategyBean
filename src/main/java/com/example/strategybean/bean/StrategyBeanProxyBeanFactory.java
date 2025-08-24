package com.example.strategybean.bean;

import com.example.strategybean.annotation.StrategyBean;
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

		StrategyBeanProxy<?> routingBeanProxy = new StrategyBeanProxy<>(beanClassName);

		return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
		                                  new Class[]{this.beanClassName},
		                                  routingBeanProxy);
	}

	private void addBeanTargets(StrategyBean annotation) {
		if (annotation == null) {
			throw new BeanDefinitionValidationException("No @StrategyBean found on " + beanClassName);
		}

		StrategyBean.Mapping[] routingTables = annotation.value();
		if (routingTables.length == 0) {
			throw new BeanDefinitionValidationException("Cannot resolve key! Define @RoutingTable, implement RoutingKeySupplier or RoutingPredicate");
		}

		Map<String, T> targetMap = Arrays.stream(routingTables)
		                                 .collect(Collectors.toMap(StrategyBean.Mapping::mappingKey, this::getBean));
		StrategyBeanContext.strategyBeanTargets.put(beanClassName, targetMap);

		StrategyBean.Otherwise otherwise = annotation.orElse();
		T orElseTarget = getBean(otherwise);
		if (orElseTarget != null) {
			StrategyBeanContext.orElseTargets.put(beanClassName, orElseTarget);
		}
	}

	@SuppressWarnings("unchecked")
	private T getBean(StrategyBean.Mapping routingTable) {
		if (routingTable.targetClass() == Object.class) {
			throw new BeanDefinitionValidationException("targetClass must be set for mappingKey: " + routingTable.mappingKey());
		}
		return (T) applicationContext.getBean(routingTable.targetClass());
	}

	@SuppressWarnings("unchecked")
	private T getBean(StrategyBean.Otherwise otherwise) {
		if (otherwise.targetClass() == Object.class) {
			return null;
		}
		return (T) applicationContext.getBean(otherwise.targetClass());
	}

	@Override
	public Class<?> getObjectType() {
		return this.beanClassName;
	}

	@Override
	public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
