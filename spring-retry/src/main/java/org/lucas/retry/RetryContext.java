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

    int getRetryCount();

    Throwable getLastThrowable();

}
