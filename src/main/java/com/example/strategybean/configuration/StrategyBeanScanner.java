package com.example.strategybean.configuration;

import com.example.strategybean.annotation.StrategyBean;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;
import java.util.stream.Collectors;

class StrategyBeanScanner extends ClassPathScanningCandidateComponentProvider {
    private static final BeanNameGenerator BEAN_NAME_GENERATOR = new StrategyBeanNameGenerator();
    private final BeanDefinitionRegistry registry;

    StrategyBeanScanner(BeanDefinitionRegistry registry) {
        super(false);
        this.registry = registry;
        addIncludeFilter(new AnnotationTypeFilter(StrategyBean.class));
    }

    Set<BeanDefinitionHolder> scan(String basePackage) {
        Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
        return candidateComponents.stream()
            .filter(component -> isCandidateComponent((AnnotatedBeanDefinition)component))
            .map(component -> new BeanDefinitionHolder(component, BEAN_NAME_GENERATOR.generateBeanName(component, registry)))
            .peek(definitionHolder -> BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry))
            .collect(Collectors.toSet());
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isInterface = beanDefinition.getMetadata().isInterface(); // 인터페이스 여부
        boolean isIndependent = beanDefinition.getMetadata().isIndependent(); // 독립 클래스 여부 (중첩된 클래스가 아닌지)
        return isInterface && isIndependent; // 인터페이스이면서 중첩되지 않은 경우에만 true 반환
    }
}
