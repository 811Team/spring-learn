package team811.util.concurrent;

import team811.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 对 {@cdoe CompletableFuture} 进行扩展
 */
class DelegatingCompletableFuture<T> extends CompletableFuture<T> {

    private final Future<T> delegate;

    public DelegatingCompletableFuture(Future<T> delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }

    /**
     * 如果这个任务还没完成，通过该方法进行取消
     *
     * @param mayInterruptIfRunning 是否进行终止
     * @return 如果这个任务被取消返回 {@code true}
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // 取消 delegate 的任务
        boolean result = this.delegate.cancel(mayInterruptIfRunning);
        /**
         * @see CompletableFuture#cancel(boolean)
         */
        super.cancel(mayInterruptIfRunning);
        return result;
    }
}
