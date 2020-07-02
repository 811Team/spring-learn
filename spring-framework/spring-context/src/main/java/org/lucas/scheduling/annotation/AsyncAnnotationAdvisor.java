package org.lucas.scheduling.annotation;

import org.aopalliance.aop.Advice;
import org.lucas.aop.Pointcut;
import org.lucas.aop.support.AbstractPointcutAdvisor;
import org.lucas.aop.support.annotation.AnnotationMatchingPointcut;
import org.lucas.beans.factory.BeanFactoryAware;
import org.lucas.lang.Nullable;
import org.lucas.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * 对符合 pointcut 方法使用 advice 进行功能增强。
 */
public class AsyncAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    /**
     * 切面逻辑
     */
    private Advice advice;

    /**
     * 切点
     */
    private Pointcut pointcut;

    /**
     * 构建切点和切面逻辑
     *
     * @param executor         线程执行器
     * @param exceptionHandler 线程异常处理器
     */
    public AsyncAnnotationAdvisor(@Nullable Supplier<Executor> executor,
                                  @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
        // 1. 异步注解类型集合
        Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<>(2);
        // 1.1. @Async 注解
        asyncAnnotationTypes.add(Async.class);
        try {
            // 1.2 尝试加载 javax.ejb.Asynchronous 类
            asyncAnnotationTypes.add((Class<? extends Annotation>)
                    ClassUtils.forName("javax.ejb.Asynchronous", AsyncAnnotationAdvisor.class.getClassLoader()));
        } catch (ClassNotFoundException ex) {
            // If EJB 3.1 API not present, simply ignore.
        }
        // 2. 创建切面逻辑
        this.advice = buildAdvice(executor, exceptionHandler);
        // 3. 创建切点
        this.pointcut = buildPointcut(asyncAnnotationTypes);
    }

    /**
     * 创建 AnnotationAsyncExecutionInterceptor 拦截器作为切面逻辑
     *
     * @param executor         执行器
     * @param exceptionHandler 异常处理器
     * @return 切面逻辑
     */
    protected Advice buildAdvice(
            @Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {
        AnnotationAsyncExecutionInterceptor interceptor = new AnnotationAsyncExecutionInterceptor(null);
        interceptor.configure(executor, exceptionHandler);
        return interceptor;
    }

    /**
     * 创建切点
     *
     * @param asyncAnnotationTypes 异步注解类型
     * @return 切点
     */
    protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
        ComposablePointcut result = null;
        // 1. 在每个注解处创建 AnnotationMatchingPointcut 作为切点
        for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
            Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
            Pointcut mpc = new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
            if (result == null) {
                result = new ComposablePointcut(cpc);
            } else {
                result.union(cpc);
            }
            result = result.union(mpc);
        }
        return (result != null ? result : Pointcut.TRUE);
    }

}
