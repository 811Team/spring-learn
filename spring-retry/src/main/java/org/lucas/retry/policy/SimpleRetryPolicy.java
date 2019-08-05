package org.lucas.retry.policy;

import org.lucas.retry.RetryPolicy;

public class SimpleRetryPolicy implements RetryPolicy {

    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    public SimpleRetryPolicy() {
        this(DEFAULT_MAX_ATTEMPTS, BinaryExceptionClassifier.defaultClassifier());
    }

}
