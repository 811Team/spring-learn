package org.lucas.retry;

public interface RecoveryCallback<T> {

    T recover(RetryContext context) throws Exception;

}
