package team811.core.task;

import team811.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

/**
 * @create: 2017-11-08
 * @description: 异步任务接口
 */
public interface AsyncListenableTaskExecutor extends AsyncTaskExecutor {

    ListenableFuture<?> submitListenable(Runnable task);

    <T> ListenableFuture<T> submitListenable(Callable<T> task);
}
