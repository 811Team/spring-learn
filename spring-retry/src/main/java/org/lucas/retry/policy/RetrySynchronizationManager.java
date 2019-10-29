package org.lucas.retry.policy;

import org.lucas.retry.RetryContext;

public class RetrySynchronizationManager {

    private static final ThreadLocal<RetryContext> context = new ThreadLocal<>();

    private RetrySynchronizationManager() {
    }

    public static RetryContext getContext() {
        RetryContext result = context.get();
        return result;
    }

    /**
     * 替换新的 RetryContext
     *
     * @param context RetryContext
     * @return 旧 RetryContext
     */
    public static RetryContext register(RetryContext context) {
        RetryContext oldContext = getContext();
        RetrySynchronizationManager.context.set(context);
        return oldContext;
    }

    /**
     * 清除当前 RetryContext
     *
     * @return 父RetryContext
     */
    public static RetryContext clear() {
        RetryContext value = getContext();
        RetryContext parent = value == null ? null : value.getParent();
        RetrySynchronizationManager.context.set(parent);
        return value;
    }

}
