package org.lucas.retry.context;

import org.lucas.retry.RetryContext;
import org.springframework.core.AttributeAccessorSupport;

public class RetryContextSupport extends AttributeAccessorSupport implements RetryContext {

    private final RetryContext parent;

    /**
     * 是否终止
     */
    private volatile boolean terminate = false;

    /**
     * 重试次数
     */
    private volatile int count;

    /**
     * 重试异常
     */
    private volatile Throwable lastException;

    public RetryContextSupport(RetryContext parent) {
        super();
        this.parent = parent;
    }

    @Override
    public RetryContext getParent() {
        return this.parent;
    }

    @Override
    public boolean isExhaustedOnly() {
        return terminate;
    }

    @Override
    public void setExhaustedOnly() {
        terminate = true;
    }

    @Override
    public int getRetryCount() {
        return count;
    }

    @Override
    public Throwable getLastThrowable() {
        return lastException;
    }

    /**
     * 重试异常保存
     *
     * @param throwable 异常对象
     */
    public void registerThrowable(Throwable throwable) {
        this.lastException = throwable;
        if (throwable != null) {
            count++;
        }
    }

    @Override
    public String toString() {
        return String.format("[RetryContext: count=%d, lastException=%s, exhausted=%b]", count, lastException,
                terminate);
    }

}
