package team811.core.task;

import team811.lang.Nullable;
import team811.util.Assert;
import team811.util.CustomizableThreadCreator;
import team811.util.concurrent.ListenableFuture;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-17
 * @description:
 */
public class SimpleAsyncTaskExecutor extends CustomizableThreadCreator
        implements AsyncListenableTaskExecutor, Serializable {

    /**
     * 装饰任务
     */
    @Nullable
    private TaskDecorator taskDecorator;

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return null;
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return null;
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        Assert.notNull(task, "Runnable must not be null");
        // 判断是否存在任务装饰对象,如果有,则将任务放入任务装饰对象中执行
        Runnable taskToUse = (this.taskDecorator != null ? this.taskDecorator.decorate(task) : task);
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
