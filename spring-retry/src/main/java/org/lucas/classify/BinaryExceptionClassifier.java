package org.lucas.classify;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BinaryExceptionClassifier<T, C> extends SubclassClassifier<Throwable, Boolean> {

    public BinaryExceptionClassifier(boolean defaultValue) {
        super(defaultValue);
    }

    public BinaryExceptionClassifier(Collection<Class<? extends Throwable>> exceptionClasses, boolean value) {
        this(!value);
        if (exceptionClasses != null) {
            Map<Class<? extends Throwable>, Boolean> map = new HashMap<Class<? extends Throwable>, Boolean>();
            for (Class<? extends Throwable> type : exceptionClasses) {
                map.put(type, !getDefault());
            }
            setTypeMap(map);
        }
    }

}
