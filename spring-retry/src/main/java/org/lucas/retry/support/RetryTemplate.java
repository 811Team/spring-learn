package org.lucas.retry.support;

import org.lucas.retry.ExhaustedRetryException;
import org.lucas.retry.RecoveryCallback;
import org.lucas.retry.RetryCallback;
import org.lucas.retry.RetryOperations;
import org.lucas.retry.RetryPolicy;
import org.lucas.retry.RetryState;

public class RetryTemplate implements RetryOperations {

    private volatile RetryPolicy retryPolicy = new SimpleRetryPolicy(3);

    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
        return doExecute(retryCallback, null, null);
    }

    protected <T, E extends Throwable> T doExecute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback,
                                                   RetryState state) throws E, ExhaustedRetryException {
        RetryPolicy retryPolicy = this.retryPolicy;
        BackOffPolicy backOffPolicy = this.backOffPolicy;

    }
}