package org.lucas.retry;

public class TerminatedRetryException extends RetryException {

    public TerminatedRetryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TerminatedRetryException(String msg) {
        super(msg);
    }

}
