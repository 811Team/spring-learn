package org.lucas.retry.policy;

import org.lucas.retry.RetryContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapRetryContextCache implements RetryContextCache {

    public static final int DEFAULT_CAPACITY = 4096;

    private Map<Object, RetryContext> map = Collections.synchronizedMap(new HashMap<>());

    private int capacity;

    public MapRetryContextCache() {
        this(DEFAULT_CAPACITY);
    }

    public MapRetryContextCache(int defaultCapacity) {
        super();
        this.capacity = defaultCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public RetryContext get(Object key) {
        return map.get(key);
    }

    @Override
    public void put(Object key, RetryContext context) {
        if (map.size() >= capacity) {
            throw new RetryCacheCapacityExceededException("Retry cache capacity limit breached. "
                    + "Do you need to re-consider the implementation of the key generator, "
                    + "or the equals and hashCode of the items that failed?");
        }
        map.put(key, context);
    }

    @Override
    public void remove(Object key) {
        map.remove(key);
    }

}
