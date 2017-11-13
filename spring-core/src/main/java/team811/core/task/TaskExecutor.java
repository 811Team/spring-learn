package team811.core.task;

import java.util.concurrent.Executor;

/**
 * @create: 2017-11-08
 * @description: 继承Executor接口
 */
@FunctionalInterface
public interface TaskExecutor extends Executor {
    @Override
    void execute(Runnable task);
}
