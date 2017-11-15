package team811.util.concurrent;

import team811.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @create: 2017-11-14
 * @description: 实现FutureTask扩展接口,兼容Runnable
 */
public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {

    public ListenableFutureTask(Callable<T> callable) {
        super(callable);
    }

    public ListenableFutureTask(Runnable runnable, @Nullable T result) {
        super(runnable, result);
    }

    @Override
    public void addCallback(ListenableFutureCallback<? super T> callback) {

    }

    @Override
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {

    }
}
