package org.lucas.core.task.support;

import org.lucas.core.task.TaskRejectedException;
import org.lucas.core.task.AsyncListenableTaskExecutor;
import org.lucas.core.task.TaskDecorator;
import org.lucas.lang.Nullable;
import org.lucas.util.Assert;
import org.lucas.util.concurrent.ListenableFuture;
import org.lucas.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

/**
 * 通过 {@code TaskDecorator} 对象重写 {@code AsyncListenableTaskExecutor}
 */
public class TaskExecutorAdapter implements AsyncListenableTaskExecutor {

    /** 任务执行器 */
    private final Executor concurrentExecutor;

    /** 任务装饰对象 */
    @Nullable
    private TaskDecorator taskDecorator;

    /** 初始化任务执行器 */
    public TaskExecutorAdapter(Executor concurrentExecutor) {
        Assert.notNull(concurrentExecutor, "Executor must not be null");
        this.concurrentExecutor = concurrentExecutor;
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        try {
            ListenableFutureTask<Object> future = new ListenableFutureTask<>(task, null);
            doExecute(this.concurrentExecutor, this.taskDecorator, future);
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        try {
            ListenableFutureTask<T> future = new ListenableFutureTask<>(task);
            doExecute(this.concurrentExecutor, this.taskDecorator, future);
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }


    /**
     * @param task         {@code Runnable}
     * @param startTimeout
     * @see #doExecute(Executor, TaskDecorator, Runnable)
     */
    @Override
    public void execute(Runnable task, long startTimeout) {
        execute(task);
    }

    /**
     * 异步执行任务
     *
     * @param task {@code Runnable}
     * @return {@code Future} 异步执行结果 ({@code Future.get() == null})
     * @see #doExecute(Executor, TaskDecorator, Runnable)
     * @see ExecutorService#submit(Runnable)
     */
    @Override
    public Future<?> submit(Runnable task) {
        try {
            if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
                return ((ExecutorService) this.concurrentExecutor).submit(task);
            } else {
                FutureTask<Object> future = new FutureTask<>(task, null);
                doExecute(this.concurrentExecutor, this.taskDecorator, future);
                return future;
            }
        } catch (RejectedExecutionException ex) {
            // 拒绝提交任务处理
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    /**
     * 异步执行任务
     *
     * @param task {@code Runnable}
     * @return {@code Future} 异步执行结果 ({@code Future.get()})
     * @see #doExecute(Executor, TaskDecorator, Runnable)
     * @see ExecutorService#submit(Callable)
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        try {
            if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
                return ((ExecutorService) this.concurrentExecutor).submit(task);
            } else {
                FutureTask<T> future = new FutureTask<>(task);
                doExecute(this.concurrentExecutor, this.taskDecorator, future);
                return future;
            }
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }


    /**
     * @param task Runnable 对象
     * @see #doExecute(Executor, TaskDecorator, Runnable)
     */
    @Override
    public void execute(Runnable task) {
        try {
            doExecute(this.concurrentExecutor, this.taskDecorator, task);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    /**
     * 使用给定的{@code Runnable} 执行任务
     *
     * @param concurrentExecutor 任务执行器
     * @param taskDecorator      任务装饰对象,可以为 {@code null}
     * @param runnable           任务对象
     * @throws RejectedExecutionException
     */
    protected void doExecute(Executor concurrentExecutor, @Nullable TaskDecorator taskDecorator, Runnable runnable)
            throws RejectedExecutionException {
        concurrentExecutor.execute(taskDecorator != null ? taskDecorator.decorate(runnable) : runnable);
    }

    public final void setTaskDecorator(TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }
}
