package org.lucas.retry.policy;

import org.lucas.classify.BinaryExceptionClassifier;
import org.lucas.retry.RetryContext;
import org.lucas.retry.RetryPolicy;
import org.lucas.retry.context.RetryContextSupport;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * 重试策略
 */
public class SimpleRetryPolicy implements RetryPolicy {

    /**
     * 默认次数
     */
    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    /**
     * 最大尝试次数
     */
    private volatile int maxAttempts;

    private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);

    public SimpleRetryPolicy() {
        this(DEFAULT_MAX_ATTEMPTS, BinaryExceptionClassifier.defaultClassifier());
    }

    /**
     * @param maxAttempts 最大重试次数
     */
    public SimpleRetryPolicy(int maxAttempts) {
        this(maxAttempts, BinaryExceptionClassifier.defaultClassifier());
    }

    /**
     * @param maxAttempts 最大重试次数
     * @param classifier  异常类型
     */
    public SimpleRetryPolicy(int maxAttempts, BinaryExceptionClassifier classifier) {
        super();
        this.maxAttempts = maxAttempts;
        this.retryableClassifier = classifier;
    }

    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions) {
        this(maxAttempts, retryableExceptions, false);
    }

    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions,
                             boolean traverseCauses) {
        this(maxAttempts, retryableExceptions, traverseCauses, false);
    }

    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions,
                             boolean traverseCauses, boolean defaultValue) {
        super();
        this.maxAttempts = maxAttempts;
        this.retryableClassifier = new BinaryExceptionClassifier(retryableExceptions, defaultValue);
        this.retryableClassifier.setTraverseCauses(traverseCauses);
    }

    /**
     * 判断是否能重试
     *
     * @param context {@code RetryContext}
     * @return {@code true} 可以重试
     */
    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        return (t == null || retryForException(t)) && context.getRetryCount() < this.maxAttempts;
    }

    /**
     * 异常是否继续重试
     *
     * @param ex 异常信息
     * @return {@code true} 可以重试
     */
    private boolean retryForException(Throwable ex) {
        return this.retryableClassifier.classify(ex);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new SimpleRetryContext(parent);
    }

    @Override
    public void close(RetryContext status) {
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        SimpleRetryContext simpleContext = ((SimpleRetryContext) context);
        simpleContext.registerThrowable(throwable);
    }

    private static class SimpleRetryContext extends RetryContextSupport {

        public SimpleRetryContext(RetryContext parent) {
            super(parent);
        }

    }

    @Override
    public String toString() {
        return ClassUtils.getShortName(getClass()) + "[maxAttempts=" + this.maxAttempts + "]";
    }

}
