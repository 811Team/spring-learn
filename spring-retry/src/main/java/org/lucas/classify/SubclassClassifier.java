package org.lucas.classify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SubclassClassifier<T, C> implements Classifier<T, C> {

    private ConcurrentMap<Class<? extends T>, C> classified = new ConcurrentHashMap<Class<? extends T>, C>();

    private C defaultValue = null;

    public SubclassClassifier() {
        this(null);
    }

    public SubclassClassifier(C defaultValue) {
        this(new HashMap<Class<? extends T>, C>(), defaultValue);
    }

    public SubclassClassifier(Map<Class<? extends T>, C> typeMap, C defaultValue) {
        super();
        this.classified = new ConcurrentHashMap<Class<? extends T>, C>(typeMap);
        this.defaultValue = defaultValue;
    }

    @Override
    public C classify(T classifiable) {
        if (classifiable == null) {
            return this.defaultValue;
        }

        Class<? extends T> exceptionClass = (Class<? extends T>) classifiable.getClass();
        if (this.classified.containsKey(exceptionClass)) {
            return this.classified.get(exceptionClass);
        }

        C value = null;
        for (Class<?> cls = exceptionClass; !cls.equals(Object.class) && value == null; cls = cls.getSuperclass()) {
            value = this.classified.get(cls);
        }
    }

}
