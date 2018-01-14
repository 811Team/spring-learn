package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.support.TaskExecutorAdapter;
import team811.lang.Nullable;
import team811.scheduling.SchedulingAwareRunnable;
import team811.scheduling.SchedulingTaskExecutor;
import team811.util.ClassUtils;

import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @create: 2017-11-21
 * @description:
 */
public class ConcurrentTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

    /**
     * {@code ExecutorService} 接口扩展
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

    /** 带装饰器的执行重构对象 */
    private TaskExecutorAdapter adaptedExecutor;

    /**
     * 创建默认异步任务执行器构建 {@code adaptedExecutor} 对象
     */
    public ConcurrentTaskExecutor() {
        this.concurrentExecutor = Executors.newSingleThreadExecutor();
        this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
    }

    /**
     * 通过指定执行器构建 {@code adaptedExecutor} 对象
     *
     * @param executor {@code Executor}
     */
    public ConcurrentTaskExecutor(@Nullable Executor executor) {
        this.concurrentExecutor = (executor != null ? executor : Executors.newSingleThreadExecutor());
        this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
    }

    private static TaskExecutorAdapter getAdaptedExecutor(Executor concurrentExecutor) {
        if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
            return new ManagedTaskExecutorAdapter(concurrentExecutor);
        }
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
    }


    protected static class ManagedTaskBuilder {

        public static Runnable buildManagedTask(Runnable task, String identityName) {
            Map<String, String> properties = new HashMap<>(2);
            if (task instanceof SchedulingAwareRunnable) {
                properties.put(ManagedTask.LONGRUNNING_HINT,
                        Boolean.toString(((SchedulingAwareRunnable) task).isLongLived()));
            }
            properties.put(ManagedTask.IDENTITY_NAME, identityName);
            return ManagedExecutors.managedTask(task, properties, null);
        }
    }
}
