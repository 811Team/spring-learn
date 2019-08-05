package org.lucas.core.convert;

import org.lucas.lang.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2018-01-28
 * @description:
 */
public class TypeDescriptor implements Serializable {

    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(18);

    /** 存放基础类类型数组 */
    private static final Class<?>[] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
            double.class, Double.class, int.class, Integer.class, long.class, Long.class,
            float.class, Float.class, short.class, Short.class, String.class, Object.class};

    static {
        for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
        }
    }

    public static TypeDescriptor valueOf(@Nullable Class<?> type) {
        if (type == null) {
            type = Object.class;
        }
        TypeDescriptor desc = commonTypesCache.get(type);
        return (desc != null ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null));
    }

}
