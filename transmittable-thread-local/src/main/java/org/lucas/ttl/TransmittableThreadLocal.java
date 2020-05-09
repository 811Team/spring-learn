package org.lucas.ttl;

import java.util.WeakHashMap;

public class TransmittableThreadLocal<T> extends InheritableThreadLocal<T> implements TtlCopier<T> {

    private final boolean disableIgnoreNullValueSemantics;

    private static InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>> holder = new InheritableThreadLocal<WeakHashMap<TransmittableThreadLocal<Object>, ?>>() {
        @Override
        protected WeakHashMap<TransmittableThreadLocal<Object>, ?> initialValue() {
            return new WeakHashMap<>();
        }

        @Override
        protected WeakHashMap<TransmittableThreadLocal<Object>, ?> childValue(WeakHashMap<TransmittableThreadLocal<Object>, ?> parentValue) {
            return new WeakHashMap<TransmittableThreadLocal<Object>, Object>(parentValue);
        }
    };

    public TransmittableThreadLocal() {
        this(false);
    }

    public TransmittableThreadLocal(boolean disableIgnoreNullValueSemantics) {
        this.disableIgnoreNullValueSemantics = disableIgnoreNullValueSemantics;
    }


    protected void beforeExecute() {
    }

    protected void afterExecute() {
    }

    @Override
    public T copy(T parentValue) {
        return parentValue;
    }

    @Override
    public final T get() {
        T value = super.get();
        if (disableIgnoreNullValueSemantics || null != value) {
            addThisToHolder();
        }
        return value;
    }

    private void addThisToHolder() {
        if (!holder.get().containsKey(this)) {
            holder.get().put((TransmittableThreadLocal<Object>) this, null); // WeakHashMap supports null value.
        }
    }
}
