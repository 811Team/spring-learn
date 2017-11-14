package team811.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-08
 * @description: 异步任务的扩展接口
 */
public interface AsyncTaskExecutor extends TaskExecutor {

    void execute(Runnable task, long startTimeout);

    Future<?> submit(Runnable task);

    <T> Future<T> submit(Callable<T> task);

}
