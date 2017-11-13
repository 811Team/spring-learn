package team811.util.concurrent;

import team811.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @create: 2017-11-13
 * @description:
 */
class DelegatingCompletableFuture<T> extends CompletableFuture<T> {

    private final Future<T> delegate;

    public DelegatingCompletableFuture(Future<T> delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }
}
