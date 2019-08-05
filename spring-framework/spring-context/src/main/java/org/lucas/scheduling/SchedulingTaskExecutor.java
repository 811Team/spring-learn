package org.lucas.scheduling;

import org.lucas.core.task.AsyncTaskExecutor;

/**
 * @create: 2017-11-08
 * @description:
 */
public interface SchedulingTaskExecutor extends AsyncTaskExecutor {

    /**
     * 线程特性,是否短期属于短期任务
     */
    default boolean prefersShortLivedTasks(){
        return true;
    }

}
