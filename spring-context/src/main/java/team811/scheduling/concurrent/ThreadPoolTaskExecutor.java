package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.TaskDecorator;
import team811.lang.Nullable;
import team811.scheduling.SchedulingTaskExecutor;
import team811.util.concurrent.ListenableFuture;

import java.util.concurrent.*;

/**
 * 线程池
 */
public class ThreadPoolTaskExecutor extends ExecutorConfigurationSupport
        implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

    /**
     * 核心线程池大小
     * <p>
     * 当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，
     * 即使此时线程池中存在空闲线程。
     * <p>
     * 当线程池达到corePoolSize时，新提交任务将被放入任务队列中.
     */
    private int corePoolSize = 1;

    /**
     * 最大线程池大小
     * <p>
     * 如果任务队列已满,将创建最大线程池的数量执行任务,如果超出最大线程池的大小,
     * 将提交给RejectedExecutionHandler处理
     */
    private int maxPoolSize = Integer.MAX_VALUE;

    /**
     * 线程池中超过核心线程数目的空闲线程最大存活时间；
     * 可以allowCoreThreadTimeOut(true)使得核心线程有效时间
     */
    private int keepAliveSeconds = 60;

    /**
     * 阻塞任务队列容量(默认为int的最大值)
     */
    private int queueCapacity = Integer.MAX_VALUE;

    /**
     * 任务装饰对象
     */
    @Nullable
    private TaskDecorator taskDecorator;

    /**
     * 线程池对象
     */
    @Nullable
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 对空闲核心线程是否进行关闭
     */
    private boolean allowCoreThreadTimeOut = false;

    @Override
    public void execute(Runnable command) {

    }

    /**
     * 初始化线程池
     *
     * @param threadFactory            线程工厂
     * @param rejectedExecutionHandler 被拒绝的处理策略对象
     * @return ExecutorService
     */
    @Override
    protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        // 创建任务队列
        BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
        ThreadPoolExecutor executor;
        if (this.taskDecorator != null) {
            /**
             * 任务装饰对象如果不为空,则重写execute方法创建线程池,调用装饰对象任务
             */
            executor = new ThreadPoolExecutor(
                    this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS,
                    queue, threadFactory, rejectedExecutionHandler) {
                @Override
                public void execute(Runnable command) {
                    super.execute(taskDecorator.decorate(command));
                }
            };
        } else {
            executor = new ThreadPoolExecutor(
                    this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS,
                    queue, threadFactory, rejectedExecutionHandler);
        }

        if (this.allowCoreThreadTimeOut) {
            /**
             * 核心线程超时是否销毁(keepAliveSeconds设置)
             */
            executor.allowCoreThreadTimeOut(true);
        }
        this.threadPoolExecutor = executor;
        return executor;
    }

    /**
     * 如果指定队列容量,将返回指定容量的LinkedBlockingQueue对象,
     * 否则返回无缓冲的队列SynchronousQueue对象
     *
     * @param queueCapacity 任务队列容量
     * @return BlockingQueue 队列
     */
    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return null;
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return null;
    }
}
