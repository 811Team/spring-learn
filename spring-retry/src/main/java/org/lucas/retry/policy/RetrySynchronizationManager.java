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

}
