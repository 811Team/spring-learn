package org.lucas.retry.support;

import org.lucas.retry.ExhaustedRetryException;
import org.lucas.retry.RecoveryCallback;
import org.lucas.retry.RetryCallback;
import org.lucas.retry.RetryContext;
import org.lucas.retry.RetryException;
import org.lucas.retry.RetryListener;
import org.lucas.retry.RetryOperations;
import org.lucas.retry.RetryPolicy;
import org.lucas.retry.RetryState;
import org.lucas.retry.TerminatedRetryException;
import org.lucas.retry.backoff.BackOffContext;
import org.lucas.retry.backoff.BackOffInterruptedException;
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

    private volatile RetryListener[] listeners = new RetryListener[0];

    private boolean throwLastExceptionOnExhausted;

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

        RetrySynchronizationManager.register(context);

        Throwable lastException = null;

        boolean exhausted = false;

        try {
            boolean running = doOpenInterceptors(retryCallback, context);

            if (!running) {
                throw new TerminatedRetryException("Retry terminated abnormally by interceptor before first attempt");
            }

            BackOffContext backOffContext = null;
            Object resource = context.getAttribute("backOffContext");

            if (resource instanceof BackOffContext) {
                backOffContext = (BackOffContext) resource;
            }

            if (backOffContext == null) {
                backOffContext = backOffPolicy.start(context);
                if (backOffContext != null) {
                    context.setAttribute("backOffContext", backOffContext);
                }
            }

            while (canRetry(retryPolicy, context) && !context.isExhaustedOnly()) {
                // 继续重试
                try {
                    lastException = null;
                    return retryCallback.doWithRetry(context);
                } catch (Throwable e) {
                    lastException = e;
                    try {
                        // 保存异常信息
                        registerThrowable(retryPolicy, state, context, e);
                    } catch (Exception ex) {
                        throw new TerminatedRetryException("Could not register throwable", ex);
                    } finally {
                        doOnErrorInterceptors(retryCallback, context, e);
                    }
                    if (canRetry(retryPolicy, context) && !context.isExhaustedOnly()) {
                        try {
                            backOffPolicy.backOff(backOffContext);
                        } catch (BackOffInterruptedException ex) {
                            lastException = e;
                            throw ex;
                        }
                    }
                    if (shouldRethrow(retryPolicy, context, state)) {
                        throw RetryTemplate.<E>wrapIfNecessary(e);
                    }
                }

                if (state != null && context.hasAttribute(GLOBAL_STATE)) {
                    break;
                }
            }
            exhausted = true;
            return handleRetryExhausted(recoveryCallback, context, state);

        } catch (Throwable e) {
            throw RetryTemplate.<E>wrapIfNecessary(e);
        } finally {
            close(retryPolicy, context, state, lastException == null || exhausted);
            doCloseInterceptors(retryCallback, context, lastException);
            RetrySynchronizationManager.clear();
        }
    }

    protected void close(RetryPolicy retryPolicy, RetryContext context, RetryState state, boolean succeeded) {
        if (state != null) {
            if (succeeded) {
                if (!context.hasAttribute(GLOBAL_STATE)) {
                    this.retryContextCache.remove(state.getKey());
                }
                retryPolicy.close(context);
                context.setAttribute(RetryContext.CLOSED, true);
            }
        }
        else {
            retryPolicy.close(context);
            context.setAttribute(RetryContext.CLOSED, true);
        }
    }

    private <T, E extends Throwable> void doCloseInterceptors(RetryCallback<T, E> callback, RetryContext context,
                                                              Throwable lastException) {
        for (int i = this.listeners.length; i-- > 0;) {
            this.listeners[i].close(context, callback, lastException);
        }
    }

    protected <T> T handleRetryExhausted(RecoveryCallback<T> recoveryCallback, RetryContext context, RetryState state)
            throws Throwable {
        context.setAttribute(RetryContext.EXHAUSTED, true);

        if (state != null && !context.hasAttribute(GLOBAL_STATE)) {
            this.retryContextCache.remove(state.getKey());
        }
        if (recoveryCallback != null) {
            T recovered = recoveryCallback.recover(context);
            context.setAttribute(RetryContext.RECOVERED, true);
            return recovered;
        }
        if (state != null) {
            rethrow(context, "Retry exhausted after last attempt with no recovery path");
        }
        throw wrapIfNecessary(context.getLastThrowable());
    }

    protected boolean shouldRethrow(RetryPolicy retryPolicy, RetryContext context, RetryState state) {
        return state != null && state.rollbackFor(context.getLastThrowable());
    }

    private <T, E extends Throwable> void doOnErrorInterceptors(RetryCallback<T, E> callback, RetryContext context,
                                                                Throwable throwable) {
        for (int i = this.listeners.length; i-- > 0; ) {
            this.listeners[i].onError(context, callback, throwable);
        }
    }

    /**
     * 根据重试策略 retryPolicy 和 context 判断是否能继续重试
     *
     * @param retryPolicy 重试策略
     * @param context     重试信息
     * @return {@code true} 可以重试
     */
    protected boolean canRetry(RetryPolicy retryPolicy, RetryContext context) {
        return retryPolicy.canRetry(context);
    }

    protected RetryContext open(RetryPolicy retryPolicy, RetryState state) {
        if (state == null) {
            return doOpenInternal(retryPolicy);
        }

        Object key = state.getKey();
        if (state.isForceRefresh()) {
            return doOpenInternal(retryPolicy, state);
        }

        // 如果没有命中缓存，将状态加入缓存。
        if (!this.retryContextCache.containsKey(key)) {
            // 发生故障的时候使用缓存
            return doOpenInternal(retryPolicy, state);
        }
        RetryContext context = this.retryContextCache.get(key);
        if (context == null) {
            if (this.retryContextCache.containsKey(key)) {
                throw new RetryException("Inconsistent state for failed item: no history found. "
                        + "Consider whether equals() or hashCode() for the item might be inconsistent, "
                        + "or if you need to supply a better ItemKeyGenerator");
            }
            // 缓存可能在处理过程中过期了，所以需要处理这个问题。
            return doOpenInternal(retryPolicy, state);
        }

        context.removeAttribute(RetryContext.CLOSED);
        context.removeAttribute(RetryContext.EXHAUSTED);
        context.removeAttribute(RetryContext.RECOVERED);
        return context;

    }

    private <T, E extends Throwable> boolean doOpenInterceptors(RetryCallback<T, E> callback, RetryContext context) {

        boolean result = true;

        for (RetryListener listener : this.listeners) {
            result = result && listener.open(context, callback);
        }

        return result;

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
            // 全局状态缓存
            registerContext(context, state);
        }
        return context;
    }

    /**
     * 缓存 RetryContext 和 RetryState 到 {@code retryContextCache}
     *
     * @param context RetryContext
     * @param state   RetryState
     */
    private void registerContext(RetryContext context, RetryState state) {
        if (state != null) {
            // 获取状态
            Object key = state.getKey();
            if (key != null) {
                if (context.getRetryCount() > 1 && !this.retryContextCache.containsKey(key)) {
                    throw new RetryException("Inconsistent state for failed item key: cache key has changed. "
                            + "Consider whether equals() or hashCode() for the key might be inconsistent, "
                            + "or if you need to supply a better key");
                }
                // 缓存当前 RetryContext 和 state。
                this.retryContextCache.put(key, context);
            }
        }
    }

    /**
     * 异常转 RetryException 处理
     *
     * @param throwable 异常
     * @return RetryException 异常
     * @throws RetryException
     */
    private static <E extends Throwable> E wrapIfNecessary(Throwable throwable) throws RetryException {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        } else if (throwable instanceof Exception) {
            @SuppressWarnings("unchecked")
            E rethrow = (E) throwable;
            return rethrow;
        } else {
            throw new RetryException("Exception in retry", throwable);
        }
    }

    /**
     * 保存异常信息
     *
     * @param retryPolicy 重试策略
     * @param state       状态
     * @param context
     * @param e
     */
    protected void registerThrowable(RetryPolicy retryPolicy, RetryState state, RetryContext context, Throwable e) {
        retryPolicy.registerThrowable(context, e);
        registerContext(context, state);
    }

    protected <E extends Throwable> void rethrow(RetryContext context, String message) throws E {
        if (this.throwLastExceptionOnExhausted) {
            @SuppressWarnings("unchecked")
            E rethrow = (E) context.getLastThrowable();
            throw rethrow;
        } else {
            throw new ExhaustedRetryException(message, context.getLastThrowable());
        }
    }

}