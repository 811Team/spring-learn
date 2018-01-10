package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.support.TaskExecutorAdapter;
import team811.lang.Nullable;
import team811.scheduling.SchedulingTaskExecutor;
import team811.util.ClassUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @create: 2017-11-21
 * @description:
 */
public class ConcurrentTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {

    @Nullable
    private static Class<?> managedExecutorServiceClass;

    static {
        managedExecutorServiceClass = ClassUtils.forName(
                "javax.enterprise.concurrent.ManagedExecutorService",
                ConcurrentTaskScheduler.class.getClassLoader());
    }

    /** 异步任务执行器 */
    private Executor concurrentExecutor;


    private TaskExecutorAdapter adaptedExecutor;

    public ConcurrentTaskExecutor() {
        // 创建异步任务执行器
        this.concurrentExecutor = Executors.newSingleThreadExecutor();
        this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
    }
}
