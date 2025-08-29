package com.example.strategybean.bean;

import com.example.strategybean.annotation.StrategyKey;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class StrategyKeyContext {
    int paramIdx = -1;
    Expression expression;
    Method method;

    StrategyKeyContext(Method method) {
        this.method = method;

        StrategyKey methodKey = AnnotationUtils.findAnnotation(method, StrategyKey.class);
        if (methodKey != null) {
            expression = new SpelExpressionParser().parseExpression(methodKey.value());
        }

        Parameter[] parameters = method.getParameters();
        if (parameters == null || parameters.length == 0) {
            return;
        }

        for (int i = 0; i < parameters.length; i++) {
            StrategyKey paramKey = AnnotationUtils.findAnnotation(parameters[i], StrategyKey.class);
            if (paramKey != null) {
                paramIdx = i;
                expression = new SpelExpressionParser().parseExpression(paramKey.value());
                break;
            }
        }
    }

    String getExpressionValue(Object[] args) {
        EvaluationContext evaluationContext = getEvaluationContext(args);
        return expression.getValue(evaluationContext, String.class);
    }

    private EvaluationContext getEvaluationContext(Object[] args) {
        EvaluationContext evaluationContext;
        if (paramIdx < 0) {
            evaluationContext = new StandardEvaluationContext(args);
        } else {
            evaluationContext = new StandardEvaluationContext(args[paramIdx]);
        }
        return evaluationContext;
    }
}
