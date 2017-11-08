package team811.scheduling.concurrent;

import team811.core.task.AsyncListenableTaskExecutor;
import team811.scheduling.SchedulingTaskExecutor;

/**
 *
 */
public class ThreadPoolTaskExecutor extends ExecutorConfigurationSupport
        implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {
    @Override
    public void execute(Runnable command) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
