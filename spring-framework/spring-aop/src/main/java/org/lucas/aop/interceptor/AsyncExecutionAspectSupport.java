package org.lucas.aop.interceptor;

import org.lucas.core.task.AsyncListenableTaskExecutor;
import org.lucas.core.task.AsyncTaskExecutor;
import org.lucas.core.task.support.TaskExecutorAdapter;
import org.lucas.lang.Nullable;
import org.lucas.util.StringUtils;
import org.lucas.util.concurrent.ListenableFuture;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AsyncExecutionAspectSupport {

    /**
     * 代理方法对应的 AsyncTaskExecutor 执行器，用于执行该代理方法
     */
    private final Map<Method, AsyncTaskExecutor> executors = new ConcurrentHashMap<>(16);

    @Nullable
    protected AsyncTaskExecutor determineAsyncExecutor(Method method) {
        // 获取对应方法的执行器
        AsyncTaskExecutor executor = this.executors.get(method);
        // 不存在则按规则查找
        if (executor == null) {
            Executor targetExecutor;
            // 如果注解 @Async 指定了执行器名称，则尝试从 Spring 的 bean 工厂获取该名称的执行器实例
            String qualifier = getExecutorQualifier(method);
            if (StringUtils.hasLength(qualifier)) {
                targetExecutor = findQualifiedExecutor(this.beanFactory, qualifier);
            } else {
                // 如果没有指定执行器名称，则获取默认执行器实例
                targetExecutor = this.defaultExecutor.get();
            }
            if (targetExecutor == null) {
                return null;
            }
            // 将执行器包装添加到缓存
            executor = (targetExecutor instanceof AsyncListenableTaskExecutor ?
                    (AsyncListenableTaskExecutor) targetExecutor : new TaskExecutorAdapter(targetExecutor));
            this.executors.put(method, executor);
        }
        // 返回方法对应的执行器
        return executor;
    }

    @Nullable
    protected Object doSubmit(Callable<Object> task, AsyncTaskExecutor executor, Class<?> returnType) {
        // 判断方法返回值是否为 CompletableFuture 类型，或者其子类，如果是则用 CompletableFuture.supplyAsync 提交到线程
        // 池执行马上返回一个 CompletableFuture。
        if (CompletableFuture.class.isAssignableFrom(returnType)) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Throwable ex) {
                    throw new CompletionException(ex);
                }
            }, executor);
        }
        // 判断方法返回值是否为 ListenableFuture 类型，或者其子类，如果是则用 CompletableFuture.supplyAsync 提交到线程
        // 池执行马上返回一个 ListenableFuture。
        else if (ListenableFuture.class.isAssignableFrom(returnType)) {
            return ((AsyncListenableTaskExecutor) executor).submitListenable(task);
        }
        // 判断方法返回值是否为 Future 类型，或者其子类，如果是则用 CompletableFuture.supplyAsync 提交到线程
        // 池执行马上返回一个 Future。
        else if (Future.class.isAssignableFrom(returnType)) {
            return executor.submit(task);
            // 其它情况下说明不需要返回值，直接提交到线程池执行。
        } else {
            executor.submit(task);
            return null;
        }
    }

}
