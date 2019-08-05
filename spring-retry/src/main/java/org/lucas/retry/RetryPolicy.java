package org.lucas.retry;

import java.io.Serializable;

public interface RetryPolicy extends Serializable {

    boolean canRetry(RetryContext context);

    RetryContext open(RetryContext parent);

    void close(RetryContext context);

    void registerThrowable(RetryContext context, Throwable throwable);
}
