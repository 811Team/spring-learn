package org.lucas.retry;

public interface RetryState {

    Object getKey();

    boolean isForceRefresh();

    boolean rollbackFor(Throwable exception);

}
