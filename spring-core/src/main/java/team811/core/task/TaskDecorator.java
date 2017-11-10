package team811.core.task;

/**
 * @create: 2017-11-10
 * @description:
 */
@FunctionalInterface
public interface TaskDecorator {
    Runnable decorate(Runnable runnable);
}
