package org.lucas.retry.support;

import org.lucas.retry.ExhaustedRetryException;
import org.lucas.retry.RecoveryCallback;
import org.lucas.retry.RetryCallback;
import org.lucas.retry.RetryContext;
import org.lucas.retry.RetryException;
import org.lucas.retry.RetryOperations;
import org.lucas.retry.RetryPolicy;
import org.lucas.retry.RetryState;
import org.lucas.retry.backoff.BackOffPolicy;
import org.lucas.retry.backoff.NoBackOffPolicy;
import org.lucas.retry.policy.MapRetryContextCache;
import org.lucas.retry.policy.RetryContextCache;
import org.lucas.retry.policy.RetrySynchronizationManager;
import org.lucas.retry.policy.SimpleRetryPolicy;

public class RetryTemplate implements RetryOperations {

    private static final String GLOBAL_STATE = "state.global";

    /**
     * 重试策略：
     * SimpleRetryPolicy：根据异常判断是否继续重试，直到最大重试次数
     */
    private volatile RetryPolicy retryPolicy = new SimpleRetryPolicy(3);

    private RetryContextCache retryContextCache = new MapRetryContextCache();

    private volatile BackOffPolicy backOffPolicy = new NoBackOffPolicy();

    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
        return doExecute(retryCallback, null, null);
    }

    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback,
                                                    RecoveryCallback<T> recoveryCallback) throws E {
        return doExecute(retryCallback, recoveryCallback, null);
    }

    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RetryState retryState)
            throws E, ExhaustedRetryException {
        return doExecute(retryCallback, null, retryState);
    }

    @Override
    public final <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback,
                                                    RecoveryCallback<T> recoveryCallback, RetryState retryState) throws E, ExhaustedRetryException {
        return doExecute(retryCallback, recoveryCallback, retryState);
    }

    protected <T, E extends Throwable> T doExecute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback,
                                                   RetryState state) throws E, ExhaustedRetryException {
        RetryPolicy retryPolicy = this.retryPolicy;
        BackOffPolicy backOffPolicy = this.backOffPolicy;

        RetryContext context = open(retryPolicy, state);

    }

    protected RetryContext open(RetryPolicy retryPolicy, RetryState state) {
        if (state == null) {
            return doOpenInternal(retryPolicy);
        }
    }

    private RetryContext doOpenInternal(RetryPolicy retryPolicy) {
        return doOpenInternal(retryPolicy, null);
    }

    private RetryContext doOpenInternal(RetryPolicy retryPolicy, RetryState state) {
        // 获取当前线程的 RetryContext，构建新的 RetryContext
        RetryContext context = retryPolicy.open(RetrySynchronizationManager.getContext());
        if (state != null) {
            // 设置 RetryContext 状态
            context.setAttribute(RetryContext.STATE_KEY, state.getKey());
        }
        if (context.hasAttribute(GLOBAL_STATE)) {
            registerContext(context, state);
        }
    }

    private void registerContext(RetryContext context, RetryState state) {
        if (state != null) {
            //
            Object key = state.getKey();
            if (key != null) {
                if (context.getRetryCount() > 1 && !this.retryContextCache.containsKey(key)) {
                    throw new RetryException("Inconsistent state for failed item key: cache key has changed. "
                            + "Consider whether equals() or hashCode() for the key might be inconsistent, "
                            + "or if you need to supply a better key");
                }
                this.retryContextCache.put(key, context);
            }
        }
    }

}