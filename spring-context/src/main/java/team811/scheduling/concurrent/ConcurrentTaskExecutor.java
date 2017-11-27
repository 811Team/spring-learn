package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.core.task.support.TaskExecutorAdapter;
import team811.scheduling.SchedulingTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
}
