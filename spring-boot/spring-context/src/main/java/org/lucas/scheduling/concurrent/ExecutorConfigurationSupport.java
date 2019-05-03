package org.lucas.scheduling.concurrent;

import org.lucas.Log;
import org.lucas.LogFactory;
import org.lucas.lang.Nullable;
import org.lucas.beans.factory.BeanNameAware;
import org.lucas.beans.factory.DisposableBean;
import org.lucas.beans.factory.InitializingBean;

import java.util.concurrent.*;

/**
 * @create: 2017-11-08
 * @description: 定义线程池配置和的生命周期的处理。
 */
public abstract class ExecutorConfigurationSupport extends CustomizableThreadFactory
        implements BeanNameAware, InitializingBean, DisposableBean {

    /**
     * 日志对象
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 当前对象
     */
    private ThreadFactory threadFactory = this;

    /**
     * 被拒绝任务的处理对象
     * <p>
     * 当提交任务数超过最大线程数时，
     * 新提交任务由RejectedExecutionHandler对象处理
     */
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 线程池
     */
    @Nullable
    private ExecutorService executor;

    /**
     * 线程名前缀是否被设置
     */
    private boolean threadNamePrefixSet = false;

    /**
     * 是否对线程池中子线程进行立即关闭
     */
    private boolean waitForTasksToCompleteOnShutdown = false;

    /**
     * 线程池超时关闭时间(小于0,不设置超时时间)
     */
    private int awaitTerminationSeconds = 0;

    @Nullable
    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * 对象初始化
     */
    @Override
    public void afterPropertiesSet() {
        initialize();
    }

    /**
     * 对象销毁
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    /**
     * 初始化线程池
     */
    public void initialize() {
        if (logger.isInfoEnabled()) {
            logger.info("Initializing ExecutorService " + (this.beanName != null ? " '" + this.beanName + "'" : ""));
        }
        if (!this.threadNamePrefixSet && this.beanName != null) {
            setThreadNamePrefix(this.beanName + "-");
        }
        this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
    }

    /**
     * 初始化线程池
     *
     * @param threadFactory            线程工厂
     * @param rejectedExecutionHandler 被拒绝的处理策略对象
     * @return ExecutorService 线程池
     */
    protected abstract ExecutorService initializeExecutor(
            ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler);

    /**
     * 设置线程名前缀
     *
     * @param threadNamePrefix 线程名前缀
     */
    @Override
    public void setThreadNamePrefix(@Nullable String threadNamePrefix) {
        super.setThreadNamePrefix(threadNamePrefix);
        this.threadNamePrefixSet = true;
    }

    /**
     * 对当前线程池进行关闭(调用该方法,并不会立即进行关闭,但会拒绝提交任务).
     */
    public void shutdown() {
        if (logger.isInfoEnabled()) {
            logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
        }
        if (this.executor != null) {
            if (this.waitForTasksToCompleteOnShutdown) {
                /**
                 * 不能再往线程池中添加任何任务，否则将会抛出RejectedExecutionException异常。
                 * 但是，线程池不会立刻退出，线程池中的任务都已经处理完成，才会退出。
                 */
                this.executor.shutdown();
            } else {
                /**
                 * 线程池的状态立刻变成停止状态，并试图停止所有正在执行的线程，
                 * 不再处理还在池队列中等待的任务，返回那些未执行的任务。
                 *
                 * (该方法通过调用Thread.interrupt()实现,所以也不能保证所有线程能够立即停止)
                 */
                for (Runnable remainingTask : this.executor.shutdownNow()) {
                    cancelRemainingTask(remainingTask);
                }
            }
            awaitTerminationIfNecessary(this.executor);
        }
    }

    /**
     * 尝试取消任务(只针对 Future 对象)
     *
     * @param task Future 任务
     * @see Future#cancel(boolean)
     */
    protected void cancelRemainingTask(Runnable task) {
        if (task instanceof Future) {
            ((Future<?>) task).cancel(true);
        }
    }

    /**
     * 根据超时时间等待当前线程池终止
     *
     * @param executor
     */
    private void awaitTerminationIfNecessary(ExecutorService executor) {
        if (this.awaitTerminationSeconds > 0) {
            try {
                // 线程池是否终止,或者超时
                if (!executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS)) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Timed out while waiting for executor" +
                                (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
                    }
                }
            } catch (InterruptedException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Interrupted while waiting for executor" +
                            (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");
                    // 对当前线程发送中断信号(有可能无法中断)
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
