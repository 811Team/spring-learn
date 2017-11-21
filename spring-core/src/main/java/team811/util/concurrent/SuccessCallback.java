package team811.util.concurrent;

import team811.lang.Nullable;

/**
 * 成功回调接口
 */
@FunctionalInterface
public interface SuccessCallback<T> {
    /**
     * 成功回调
     *
     * @param result 成功结果
     */
    void onSuccess(@Nullable T result);
}
