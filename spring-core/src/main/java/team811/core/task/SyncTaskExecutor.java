package team811.core.task;

import team811.util.Assert;

import java.io.Serializable;

/**
 * 线程任务执行
 */
public class SyncTaskExecutor implements TaskExecutor, Serializable {

    /**
     * @param task Runnable 对象
     * @throws IllegalArgumentException
     */
    @Override
    public void execute(Runnable task) {
        Assert.notNull(task, "Runnable must not be null");
        task.run();
    }
}
