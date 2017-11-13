package team811.util.concurrent;

import team811.lang.Nullable;

/**
 * @create: 2017-11-13
 * @description:
 */
@FunctionalInterface
public interface SuccessCallback<T> {
    void onSuccess(@Nullable T result);
}
