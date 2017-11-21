package team811.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-08
 * @description: 异步任务的扩展接口
 */
public interface AsyncTaskExecutor extends TaskExecutor {

    /** 表示立即执行的常量 */
    long TIMEOUT_IMMEDIATE = 0;

    /** 表示没有时间限制 */
    long TIMEOUT_INDEFINITE = Long.MAX_VALUE;

    /**
     * 执行线程任务
     *
     * @param task         Runnable 对象任务类型
     * @param startTimeout
     */
    void execute(Runnable task, long startTimeout);

    Future<?> submit(Runnable task);

    <T> Future<T> submit(Callable<T> task);

}
