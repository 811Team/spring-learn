package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.support.TaskExecutorAdapter;
import team811.scheduling.SchedulingTaskExecutor;
import team811.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-21
 * @description:
 */
public class ConcurrentTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

    /** 异步任务执行器 */
    private Executor concurrentExecutor;

    private TaskExecutorAdapter adaptedExecutor;

    public ConcurrentTaskExecutor() {
        // 创建异步任务执行器
        this.concurrentExecutor = Executors.newSingleThreadExecutor();
        this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return null;
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return null;
    }

    @Override
    public boolean prefersShortLivedTasks() {
        return false;
    }

    @Override
    public void execute(Runnable task, long startTimeout) {

    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public void execute(Runnable task) {

    }
}
