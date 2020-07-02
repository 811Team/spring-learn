package org.lucas.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);

    boolean isRuntime();

    boolean matches(Method method, Class<?> targetClass, Object... args);


    /**
     * 匹配所有方法的 TrueMethodMatcher 实例。
     */
    MethodMatcher TRUE = TrueMethodMatcher.INSTANCE;
}
