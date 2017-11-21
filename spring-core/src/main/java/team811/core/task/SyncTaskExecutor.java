package team811.core.task;

import team811.util.Assert;

import java.io.Serializable;

/**
 * 不通过异步方法来执行,直接运行
 */
public class SyncTaskExecutor implements TaskExecutor, Serializable {

    /**
     * 直接运行任务,不另起线程
     *
     * @param task Runnable 对象
     * @throws IllegalArgumentException
     */
    @Override
    public void execute(Runnable task) {
        Assert.notNull(task, "Runnable must not be null");
        task.run();
    }
}
