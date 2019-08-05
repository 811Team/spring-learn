package org.lucas.retry;

import org.springframework.core.NestedRuntimeException;

public class RetryException extends NestedRuntimeException {

    public RetryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RetryException(String msg) {
        super(msg);
    }

}
