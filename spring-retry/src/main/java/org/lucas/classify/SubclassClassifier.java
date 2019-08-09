package org.lucas.classify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SubclassClassifier<T, C> implements Classifier<T, C> {

    private ConcurrentMap<Class<? extends T>, C> classified = new ConcurrentHashMap<>();

    private C defaultValue = null;

    public SubclassClassifier() {
        this(null);
    }

    public SubclassClassifier(C defaultValue) {
        this(new HashMap<>(), defaultValue);
    }

    public SubclassClassifier(Map<Class<? extends T>, C> typeMap, C defaultValue) {
        super();
        this.classified = new ConcurrentHashMap<>(typeMap);
        this.defaultValue = defaultValue;
    }

    @Override
    public C classify(T classifiable) {
        if (classifiable == null) {
            // 默认值
            return this.defaultValue;
        }

        Class<? extends T> exceptionClass = (Class<? extends T>) classifiable.getClass();
        // classified 中有该key直接返回
        if (this.classified.containsKey(exceptionClass)) {
            return this.classified.get(exceptionClass);
        }

        C value = null;
        // 通过父类获取
        for (Class<?> cls = exceptionClass; !cls.equals(Object.class) && value == null; cls = cls.getSuperclass()) {
            value = this.classified.get(cls);
        }

        if (value == null) {
            // 通过接口获取，包含父类接口
            for (Class<?> cls = exceptionClass; !cls.equals(Object.class) && value == null; cls = cls.getSuperclass()) {
                for (Class<?> ifc : cls.getInterfaces()) {
                    value = this.classified.get(ifc);
                    if (value != null) {
                        break;
                    }
                }
            }
        }

        if (value != null) {
            // 缓存
            this.classified.put(exceptionClass, value);
        }

        if (value == null) {
            // 还未找到则返回默认值
            value = this.defaultValue;
        }

        return value;
    }

}
