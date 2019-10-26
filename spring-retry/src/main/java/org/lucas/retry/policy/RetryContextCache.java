package org.lucas.retry.policy;

import org.lucas.retry.RetryContext;

public interface RetryContextCache {

    RetryContext get(Object key);

    void put(Object key, RetryContext context) throws RetryCacheCapacityExceededException;

    void remove(Object key);

    boolean containsKey(Object key);

}
