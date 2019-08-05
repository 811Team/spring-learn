package org.lucas.core.task;

import java.util.concurrent.Executor;

/**
 * @create: 2017-11-08
 * @description: 继承Executor接口(函数接口)
 */
@FunctionalInterface
public interface TaskExecutor extends Executor {

    /**
     * 执行任务
     *
     * @param task Runnable 对象
     */
    @Override
    void execute(Runnable task);
}
