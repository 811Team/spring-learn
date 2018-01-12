package team811.util.concurrent;

import team811.lang.Nullable;

import java.util.concurrent.FutureTask;

/**
 * 当 {@code FutureTask} 运行没有异常，将回调该接口方法
 */
@FunctionalInterface
public interface SuccessCallback<T> {
    /**
     * 成功回调
     *
     * @param result {@link FutureTask#get()}
     */
    void onSuccess(@Nullable T result);
}
