package org.lucas.retry;

public interface RetryOperations {

    /**
     * 重试方法
     *
     * @param retryCallback 重试任务
     * @return 执行结果
     */
    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;

    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback)
            throws E;

    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RetryState retryState)
            throws E, ExhaustedRetryException;

    <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback,
                                       RetryState retryState) throws E;

}
