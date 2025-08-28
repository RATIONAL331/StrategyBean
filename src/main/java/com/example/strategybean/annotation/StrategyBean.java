package com.example.strategybean.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrategyBean {
    String name() default "";

    Mapping[] value() default {};

    Otherwise orElse() default @Otherwise;

    @interface Mapping {
        String mappingKey();

        Class<?> targetClass() default Object.class;
    }

    @interface Otherwise {
        Class<?> targetClass() default Object.class;
    }
}
