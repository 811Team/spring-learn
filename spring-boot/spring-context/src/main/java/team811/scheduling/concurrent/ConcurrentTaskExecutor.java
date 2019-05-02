package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.TaskDecorator;
import team811.core.task.support.TaskExecutorAdapter;
import team811.lang.Nullable;
import team811.scheduling.SchedulingAwareRunnable;
import team811.scheduling.SchedulingTaskExecutor;
import team811.util.ClassUtils;
import team811.util.concurrent.ListenableFuture;

import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 通过 {@code TaskExecutorAdapter} 执行任务
 *
 * 可以通过 {@code TaskExecutorAdapter} 在任务运行前后做某些功能操作
 */
public class ConcurrentTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

    /**
     * {@code ExecutorService} 扩展接口
     */
    @Nullable
    private static Class<?> managedExecutorServiceClass;

    static {
        try {
            managedExecutorServiceClass = ClassUtils.forName(
                    "javax.enterprise.concurrent.ManagedExecutorService",
                    ConcurrentTaskScheduler.class.getClassLoader());
        } catch (ClassNotFoundException ex) {
            managedExecutorServiceClass = null;
        }

    }

    /** 异步任务执行器 */
    private Executor concurrentExecutor;

    /** 带装饰器（TaskDecorator）的执行器 */
    private TaskExecutorAdapter adaptedExecutor;

    /**
     * 初始化属性
     * {@code concurrentExecutor} 默认 ：{@link Executors#newSingleThreadExecutor()}
     * {@code adaptedExecutor} 默认：通过{@link Executors#newSingleThreadExecutor()} 构建执行器 {@code adaptedExecutor}
     */
    public ConcurrentTaskExecutor() {
        this.concurrentExecutor = Executors.newSingleThreadExecutor();
        this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
    }

    /**
     * 初始化属性
     * {@code concurrentExecutor} 参数指定
     * {@code adaptedExecutor} 默认通过 {@link #getAdaptedExecutor(Executor)} 创建
     *
     * @param executor {@code Executor}
     */
    public ConcurrentTaskExecutor(@Nullable Executor executor) {
        this.concurrentExecutor = (executor != null ? executor : Executors.newSingleThreadExecutor());
        this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
    }

    @Override
    public void execute(Runnable task) {
        this.adaptedExecutor.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        this.adaptedExecutor.execute(task, startTimeout);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.adaptedExecutor.submit(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return this.adaptedExecutor.submit(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return this.adaptedExecutor.submitListenable(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return this.adaptedExecutor.submitListenable(task);
    }

    /**
     * 指定一个 {@code Executor} 运行 {@code Runnable}，同时构建 {@code adaptedExecutor}
     *
     * @param executor {@code Executor}
     */
    public final void setConcurrentExecutor(@Nullable Executor executor) {
        this.concurrentExecutor = (executor != null ? executor : Executors.newSingleThreadExecutor());
        this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
    }

    /**
     * 返回当前 {@code Executor} 对象
     *
     * @return {@code Executor}
     */
    public final Executor getConcurrentExecutor() {
        return this.concurrentExecutor;
    }

    /**
     * 指定一个 {@code TaskDecorator} 运行 {@code Runnable}
     *
     * @param taskDecorator {@code TaskDecorator}
     */
    public final void setTaskDecorator(TaskDecorator taskDecorator) {
        this.adaptedExecutor.setTaskDecorator(taskDecorator);
    }

    /**
     * 如果 {@code concurrentExecutor} 能够转化成 {@link javax.enterprise.concurrent.ManagedExecutorService}
     * 则返回 {@code ManagedTaskExecutorAdapter}，
     * 否则通过 {@code concurrentExecutor} 构建 {@code TaskExecutorAdapter}
     *
     * @param concurrentExecutor {@code Executor}
     * @return 通过 {@code concurrentExecutor} 构建的 {@code TaskExecutorAdapter}
     * @see ManagedTaskExecutorAdapter
     */
    private static TaskExecutorAdapter getAdaptedExecutor(Executor concurrentExecutor) {
        // 判断 concurrentExecutor 是否能转化成 managedExecutorServiceClass
        if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
            // 通过 concurrentExecutor 构建 ManagedTaskExecutorAdapter 对象
            return new ManagedTaskExecutorAdapter(concurrentExecutor);
        }
        return new TaskExecutorAdapter(concurrentExecutor);
    }

    /**
     * 默认短期任务
     *
     * @return {@code true}
     */
    @Override
    public boolean prefersShortLivedTasks() {
        return true;
    }

    /**
     * 对 {@code TaskExecutorAdapter} 进行扩展
     */
    private static class ManagedTaskExecutorAdapter extends TaskExecutorAdapter {

        public ManagedTaskExecutorAdapter(Executor concurrentExecutor) {
            super(concurrentExecutor);
        }

        @Override
        public void execute(Runnable task) {
            super.execute(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable task) {
            return super.submitListenable(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(ManagedTaskBuilder.buildManagedTask(task, task.toString()));
        }
    }

    /**
     * 构建任务
     */
    protected static class ManagedTaskBuilder {

        /**
         * 适配 {@code ManagedTask} 返回 {@code RunnableAdapter}
         *
         * @param task         {@code Runnable} 对象
         * @param identityName Runnable 字符串名称
         * @return 返回 {@code RunnableAdapter}
         * @see javax.enterprise.concurrent.ManagedExecutors.RunnableAdapter
         */
        public static Runnable buildManagedTask(Runnable task, String identityName) {
            /**
             * properties:包含两条信息
             * 1）javax.enterprise.concurrent.LONGRUNNING_HINT：{@code boolean} 该 Runnable 是否长期任务
             * 2）javax.enterprise.concurrent.IDENTITY_NAME：{@code String} 该 Runnable 的字符串名称
             */
            Map<String, String> properties = new HashMap<>(2);
            if (task instanceof SchedulingAwareRunnable) {
                properties.put(ManagedTask.LONGRUNNING_HINT,
                        Boolean.toString(((SchedulingAwareRunnable) task).isLongLived()));
            }
            properties.put(ManagedTask.IDENTITY_NAME, identityName);
            return ManagedExecutors.managedTask(task, properties, null);
        }

        /**
         * 适配 {@code ManagedTask} 返回 {@code CallableAdapter}
         *
         * @param task         {@code Callable} 对象
         * @param identityName Callable 字符串名称
         * @return 返回 {@code CallableAdapter}
         * @see javax.enterprise.concurrent.ManagedExecutors.CallableAdapter
         */
        public static <T> Callable<T> buildManagedTask(Callable<T> task, String identityName) {
            /**
             * properties:
             * 1）javax.enterprise.concurrent.IDENTITY_NAME：{@code String} 该 Runnable 的字符串名称
             */
            Map<String, String> properties = new HashMap<>(1);
            properties.put(ManagedTask.IDENTITY_NAME, identityName);
            return ManagedExecutors.managedTask(task, properties, null);
        }
    }
}
