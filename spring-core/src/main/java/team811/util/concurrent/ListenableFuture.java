package team811.util.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-13
 * @description: 线程回调接口
 */
public interface ListenableFuture<T> extends Future<T> {

    default CompletableFuture<T> completable() {
        CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
        addCallback(completable::complete, completable::completeExceptionally);
        return completable;
    }

    void addCallback(ListenableFutureCallback<? super T> callback);

    void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback);
}
