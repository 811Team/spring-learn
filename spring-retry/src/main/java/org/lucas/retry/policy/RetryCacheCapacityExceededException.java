package org.lucas.retry.policy;

import org.lucas.retry.RetryException;

public class RetryCacheCapacityExceededException extends RetryException {

    public RetryCacheCapacityExceededException(String message) {
        super(message);
    }

    public RetryCacheCapacityExceededException(String msg, Throwable nested) {
        super(msg, nested);
    }

}
