package org.lucas.retry;

import java.io.Serializable;

public interface RetryPolicy extends Serializable {

    /**
     * 判断是否能重试
     *
     * @param context {@code RetryContext}
     * @return {@code true} 可以重试
     */
    boolean canRetry(RetryContext context);

    /**
     * 构建一个新的 {@code RetryContext}
     *
     * @param parent 上次 {@code RetryContext}
     * @return {@code RetryContext}
     */
    RetryContext open(RetryContext parent);

    void close(RetryContext context);

    /**
     * 保存异常信息
     *
     * @param context   RetryContext
     * @param throwable Throwable
     */
    void registerThrowable(RetryContext context, Throwable throwable);
}
