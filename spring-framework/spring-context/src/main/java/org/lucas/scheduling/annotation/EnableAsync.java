package org.lucas.scheduling.annotation;

import org.lucas.context.annotation.AdviceMode;
import org.lucas.context.annotation.Import;
import org.lucas.core.Ordered;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 默认通过 ConfigurationClassPostProcessor 处理，
 * 解析 AsyncConfigurationSelector 中的实例注入到 spring 容器中。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AsyncConfigurationSelector.class)
public @interface EnableAsync {

    Class<? extends Annotation> annotation() default Annotation.class;

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

    int order() default Ordered.LOWEST_PRECEDENCE;

}
