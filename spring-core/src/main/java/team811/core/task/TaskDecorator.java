package team811.core.task;

/**
 * 任务装饰接口
 * <p>
 * 作用:实现该类在任务运行前后进行操作（如日志，监控）
 */
@FunctionalInterface
public interface TaskDecorator {
    /**
     * 操作任务
     *
     * @param runnable Runnable 任务对象
     * @return Runnable 实际运行任务
     */
    Runnable decorate(Runnable runnable);
}
