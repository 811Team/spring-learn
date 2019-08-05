package org.lucas.retry;

public interface RetryOperations {

    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

}
