package org.lucas.classify;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 重试异常收集
 */
public class BinaryExceptionClassifier<T, C> extends SubclassClassifier<Throwable, Boolean> {

    private boolean traverseCauses;

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

    public BinaryExceptionClassifier(Collection<Class<? extends Throwable>> exceptionClasses) {
        this(exceptionClasses, true);
    }

    public BinaryExceptionClassifier(Map<Class<? extends Throwable>, Boolean> typeMap, boolean defaultValue) {
        super(typeMap, defaultValue);
    }

    public BinaryExceptionClassifier(Map<Class<? extends Throwable>, Boolean> typeMap) {
        this(typeMap, false);
    }

    public BinaryExceptionClassifier(Map<Class<? extends Throwable>, Boolean> typeMap, boolean defaultValue,
                                     boolean traverseCauses) {
        super(typeMap, defaultValue);
        this.traverseCauses = traverseCauses;
    }

    public static BinaryExceptionClassifier defaultClassifier() {
        // 由于可变性，每个调用将创建一个新的实例。
        return new BinaryExceptionClassifier(
                Collections.<Class<? extends Throwable>, Boolean>singletonMap(Exception.class, true), false);
    }

    public void setTraverseCauses(boolean traverseCauses) {
        this.traverseCauses = traverseCauses;
    }

    @Override
    public Boolean classify(Throwable classifiable) {
        Boolean classified = super.classify(classifiable);
        if (!this.traverseCauses) {
            return classified;
        }

        /*
         * 判断是否默认，如果是默认则判断是什么原因
         */
        if (classified.equals(this.getDefault())) {
            Throwable cause = classifiable;
            do {
                if (this.getClassified().containsKey(cause.getClass())) {
                    // 能通过 classified 找到非默认分类
                    return classified;
                }
                // 获取异常原因
                cause = cause.getCause();
                classified = super.classify(cause);
            }
            while (cause != null && classified.equals(this.getDefault()));
        }

        return classified;
    }

}
