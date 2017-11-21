package team811.util.concurrent;

import team811.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @create: 2017-11-14
 * @description: 实现FutureTask扩展接口
 */
public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {

    private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<>();

    public ListenableFutureTask(Callable<T> callable) {
        super(callable);
    }

    public ListenableFutureTask(Runnable runnable, @Nullable T result) {
        super(runnable, result);
    }

    /**
     * 将回调添加到注册中心
     *
     * @param callback {@code ListenableFutureCallback}
     * @see ListenableFutureCallbackRegistry#addCallback(ListenableFutureCallback)
     */
    @Override
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        this.callbacks.addCallback(callback);
    }

    /**
     * @param successCallback {@code SuccessCallback}
     * @param failureCallback {@code FailureCallback}
     * @see ListenableFutureCallbackRegistry#addSuccessCallback(SuccessCallback)
     * @see ListenableFutureCallbackRegistry#addFailureCallback(FailureCallback)
     */
    @Override
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
        this.callbacks.addSuccessCallback(successCallback);
        this.callbacks.addFailureCallback(failureCallback);
    }
}
