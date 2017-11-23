package team811.scheduling;

import team811.core.task.AsyncTaskExecutor;

/**
 * @create: 2017-11-08
 * @description:
 */
public interface SchedulingTaskExecutor extends AsyncTaskExecutor {
    /**
     * 是否短期属于短期任务
     */
    boolean prefersShortLivedTasks();
}
