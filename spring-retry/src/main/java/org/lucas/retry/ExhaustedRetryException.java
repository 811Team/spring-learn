package org.lucas.retry;

public class ExhaustedRetryException extends RetryException {

    public ExhaustedRetryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExhaustedRetryException(String msg) {
        super(msg);
    }

}
