package org.lucas.retry;

public interface RetryListener {

    <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback);

    <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable);

    <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable);

}
