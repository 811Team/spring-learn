package org.lucas.retry.backoff;

import org.lucas.retry.RetryException;

public class BackOffInterruptedException extends RetryException {

    public BackOffInterruptedException(String msg) {
        super(msg);
    }

    public BackOffInterruptedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
