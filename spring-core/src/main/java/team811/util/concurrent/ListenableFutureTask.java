package team811.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @create: 2017-11-14
 * @description:
 */
public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {

    public ListenableFutureTask(Callable<T> callable) {
        super(callable);
    }

    @Override
    public void addCallback(ListenableFutureCallback<? super T> callback) {

    }

    @Override
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {

    }
}
