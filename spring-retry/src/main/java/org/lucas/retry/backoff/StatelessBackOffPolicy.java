package org.lucas.retry.backoff;

import org.lucas.retry.RetryContext;

public abstract class StatelessBackOffPolicy implements BackOffPolicy {

    @Override
    public final void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        doBackOff();
    }

    @Override
    public BackOffContext start(RetryContext status) {
        return null;
    }

    protected abstract void doBackOff() throws BackOffInterruptedException;

}
