package team811.util.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-13
 * @description: 线程回调接口
 */
public interface ListenableFuture<T> extends Future<T> {

    /**
     * 返回回调结果
     * <p>
     * 将{@link CompletableFuture#complete(Object)}和{@link CompletableFuture#completeExceptionally(Throwable)}
     * 引用给{@link #addCallback(SuccessCallback, FailureCallback)}
     *
     * @see CompletableFuture#complete(Object)
     * @see CompletableFuture#completeExceptionally(Throwable)
     */
    default CompletableFuture<T> completable() {
        CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
        addCallback(completable::complete, completable::completeExceptionally);
        return completable;
    }

    void addCallback(ListenableFutureCallback<? super T> callback);

    void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback);
}
