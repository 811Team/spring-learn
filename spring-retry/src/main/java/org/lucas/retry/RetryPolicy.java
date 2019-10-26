package org.lucas.retry;

import java.io.Serializable;

public interface RetryPolicy extends Serializable {

    boolean canRetry(RetryContext context);

    /**
     * 构建一个新的 {@code RetryContext}
     *
     * @param parent 上次 {@code RetryContext}
     * @return {@code RetryContext}
     */
    RetryContext open(RetryContext parent);

    void close(RetryContext context);

    void registerThrowable(RetryContext context, Throwable throwable);
}
