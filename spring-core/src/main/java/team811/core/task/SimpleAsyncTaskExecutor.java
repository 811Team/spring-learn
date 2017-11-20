package team811.core.task;

import team811.lang.Nullable;
import team811.util.Assert;
import team811.util.ConcurrencyThrottleSupport;
import team811.util.CustomizableThreadCreator;
import team811.util.concurrent.ListenableFuture;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * @create: 2017-11-17
 * @description:
 */
public class SimpleAsyncTaskExecutor extends CustomizableThreadCreator
        implements AsyncListenableTaskExecutor, Serializable {

    /** 线程工厂 */
    @Nullable
    private ThreadFactory threadFactory;

    /** 装饰任务 */
    @Nullable
    private TaskDecorator taskDecorator;

    /** 并发量控制对象 */
    private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter();

    /**
     * 执行线程任务,同时通过{@link ConcurrencyThrottleAdapter}控制并发量
     *
     * @param task         Runnable任务对象
     * @param startTimeout
     * @see #doExecute(Runnable)
     * @see #TIMEOUT_IMMEDIATE
     * @see ConcurrencyThrottlingRunnable#run()
     */
    @Override
    public void execute(Runnable task, long startTimeout) {
        Assert.notNull(task, "Runnable must not be null");
        // 判断是否存在任务装饰对象,如果有,则将任务放入任务装饰对象中执行
        Runnable taskToUse = (this.taskDecorator != null ? this.taskDecorator.decorate(task) : task);
        if (isThrottleActive() && startTimeout > TIMEOUT_IMMEDIATE) {
            // 执行前判断是否超过现有的并发量
            this.concurrencyThrottle.beforeAccess();
            doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
        } else {
            doExecute(taskToUse);
        }
    }

    /**
     * @param task Runnable对象
     * @see #createThread(Runnable)
     * @see java.lang.Thread#start()
     */
    protected void doExecute(Runnable task) {
        // 如果有线程工厂,则通过线程工厂创建线程,否则直接创建
        Thread thread = (this.threadFactory != null ? this.threadFactory.newThread(task) : createThread(task));
        thread.start();
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

    /**
     * 判断设置的并发量是否大于0
     *
     * @return boolean
     */
    public final boolean isThrottleActive() {
        return this.concurrencyThrottle.isThrottleActive();
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return null;
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return null;
    }

    public void setThreadFactory(@Nullable ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    /**
     * 并发量控制对象
     */
    private static class ConcurrencyThrottleAdapter extends ConcurrencyThrottleSupport {

        /**
         * @see ConcurrencyThrottleSupport#beforeAccess()
         */
        @Override
        protected void beforeAccess() {
            super.beforeAccess();
        }

        /**
         * @see ConcurrencyThrottleSupport#afterAccess()
         */
        @Override
        protected void afterAccess() {
            super.afterAccess();
        }
    }

    /**
     * 该对象通过回调 {@link ConcurrencyThrottleAdapter#afterAccess()}
     * 完成任务之后唤醒睡眠线程达到控制并发量的作用
     */
    private class ConcurrencyThrottlingRunnable implements Runnable {

        private final Runnable target;

        public ConcurrencyThrottlingRunnable(Runnable target) {
            this.target = target;
        }

        @Override
        public void run() {
            try {
                this.target.run();
            } finally {
                concurrencyThrottle.afterAccess();
            }
        }
    }

}
