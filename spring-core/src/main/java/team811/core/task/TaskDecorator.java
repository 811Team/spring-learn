package team811.core.task;

/**
 * @create: 2017-11-10
 * @description: 任务装饰接口
 */
@FunctionalInterface
public interface TaskDecorator {
    Runnable decorate(Runnable runnable);
}
