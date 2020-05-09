package org.lucas.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.lucas.aop.support.AopUtils;
import org.lucas.core.BridgeMethodResolver;
import org.lucas.core.task.AsyncTaskExecutor;
import org.lucas.util.ClassUtils;

import java.lang.reflect.Method;

public class AsyncExecutionInterceptor extends AsyncExecutionAspectSupport implements MethodInterceptor, Ordered {

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        // 获取被代理的对象
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        // 获取被代理的对象
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);

        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 判断使用哪种 executor 执行被代理的方法
        AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
    }
}
