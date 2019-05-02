package team811.util.concurrent;

import team811.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    /**
     * 构建 {@code DelegatingCompletableFuture}，
     * 将 {@code FutureTask} 对象委托 {@code DelegatingCompletableFuture} 进行管理
     *
     * @return {@code DelegatingCompletableFuture}
     */
    @Override
    public CompletableFuture<T> completable() {
        CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
        /**
         * 将获取结果的方法以及异常信息注册到 {@code ListenableFutureCallbackRegistry}
         */
        this.callbacks.addSuccessCallback(completable::complete);
        this.callbacks.addFailureCallback(completable::completeExceptionally);
        return completable;
    }

    /**
     * 当 {@code FutureTask} 结束时（不管成功或失败），调用该方法
     */
    @Override
    protected void done() {
        Throwable cause;
        try {
            // 获取执行结果
            T result = get();
            // 执行成功回调
            this.callbacks.success(result);
            return;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException ex) {
            cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
        } catch (Throwable ex) {
            cause = ex;
        }
        this.callbacks.failure(cause);
    }
}
