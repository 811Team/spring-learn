package org.lucas.retry;

import org.springframework.core.AttributeAccessor;

public interface RetryContext extends AttributeAccessor {

    String NAME = "context.name";

    String STATE_KEY = "context.state";

    String CLOSED = "context.closed";

    String RECOVERED = "context.recovered";

    String EXHAUSTED = "context.exhausted";

    void setExhaustedOnly();

    boolean isExhaustedOnly();

    RetryContext getParent();

    /**
     * 计算重试尝试的次数。
     * 在第一次尝试之前，该计数器为零，在第一次和随后的尝试之前，会相应地递增。
     *
     * @return 重试次数
     */
    int getRetryCount();

    /**
     * 导致重试的最后一个异常，或者可能是 {@code null}。
     * 如果这是第一次尝试，它将是空的，但如果封闭的策略决定不提供它(例如，因为担心内存使用)
     *
     * @return 异常对象
     */
    Throwable getLastThrowable();

}
