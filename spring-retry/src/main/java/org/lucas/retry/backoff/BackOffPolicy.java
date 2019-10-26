package org.lucas.retry.backoff;

import org.lucas.retry.RetryContext;

public interface BackOffPolicy {

    BackOffContext start(RetryContext context);

    void backOff(BackOffContext backOffContext) throws BackOffInterruptedException;
}
