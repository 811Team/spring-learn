package org.lucas.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.lucas.aop.support.AopUtils;
import org.lucas.core.BridgeMethodResolver;
import org.lucas.core.task.AsyncTaskExecutor;
import org.lucas.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncExecutionInterceptor extends AsyncExecutionAspectSupport implements MethodInterceptor, Ordered {

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        // 获取被代理的目标对象的 Class 对象
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        // 获取被代理的方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);

        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 根据规则获取使用哪个执行器 AsyncTaskExecutor 执行被代理的方法
        AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);

        if (executor == null) {
            throw new IllegalStateException(
                    "No executor specified and no default executor set on AsyncExecutionInterceptor either");
        }
        // 使用 Callable 执行要包装的方法
        Callable<Object> task = () -> {
            try {
                Object result = invocation.proceed();
                if (result instanceof Future) {
                    return ((Future<?>) result).get();
                }
            } catch (ExecutionException ex) {
                handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
            } catch (Throwable ex) {
                handleError(ex, userDeclaredMethod, invocation.getArguments());
            }
            return null;
        };
        // 提交包装的 Callable 到指定的执行器执行（之前执行的都是调用线程）
        return doSubmit(task, executor, invocation.getMethod().getReturnType());
    }
}
