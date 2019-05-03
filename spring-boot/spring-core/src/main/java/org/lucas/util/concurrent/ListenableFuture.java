package org.lucas.util.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-13
 * @description: 线程回调接口
 */
public interface ListenableFuture<T> extends Future<T> {

    /**
     * 获取线程{@code CompletableFuture}回调结果
     * <p>
     * 将{@link CompletableFuture#complete(Object)}和{@link CompletableFuture#completeExceptionally(Throwable)}
     * 引用给{@link #addCallback(SuccessCallback, FailureCallback)}
     *
     * @return {@code CompletableFuture}
     * @see CompletableFuture#complete(Object) 任务结果
     * @see CompletableFuture#completeExceptionally(Throwable) 任务异常信息
     */
    default CompletableFuture<T> completable() {
        CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
        addCallback(completable::complete, completable::completeExceptionally);
        return completable;
    }

    void addCallback(ListenableFutureCallback<? super T> callback);

    void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback);
}
