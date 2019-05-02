package team811.core.task;

import team811.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

/**
 * @create: 2017-11-08
 * @description: 异步任务接口
 */
public interface AsyncListenableTaskExecutor extends AsyncTaskExecutor {

    /**
     * 异步任务执行
     *
     * @param task Runnable对象
     * @return ListenableFuture(实际返回为null)
     */
    ListenableFuture<?> submitListenable(Runnable task);

    /**
     * 异步任务执行
     *
     * @param task Callable对象
     * @return ListenableFuture(实际返回为Callable任务结果)
     */
    <T> ListenableFuture<T> submitListenable(Callable<T> task);
}
