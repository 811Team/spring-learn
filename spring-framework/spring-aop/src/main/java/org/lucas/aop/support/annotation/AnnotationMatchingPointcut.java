package org.lucas.aop.support.annotation;

import org.lucas.aop.ClassFilter;
import org.lucas.aop.Pointcut;

public class AnnotationMatchingPointcut implements Pointcut {

    private static class AnnotationCandidateClassFilter implements ClassFilter {

        /**
         * 判断某个方法是否满足切点条件
         * 1. AsyncAnnotationAdvisor：判断方法是否有 @Async 注解
         *
         * @param clazz
         * @return 如果 {@code true} 满足
         */
        @Override
        public boolean matches(Class<?> clazz) {
            return AnnotationUtils.isCandidateClass(clazz, this.annotationType);
        }
    }

}
