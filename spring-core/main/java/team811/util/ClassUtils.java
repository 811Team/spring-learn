package team811.util;

import team811.lang.Nullable;

import java.util.*;

/**
 * @create: 2017-06-30
 * @description: 知识点:
 * (一)IdentityHashMap:
 * key值可以重复的map对象;
 * 取值时,通过判断key是否为同一个对象，而不是普通HashMap的equals方式判断;
 */
public abstract class ClassUtils {

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);

    /**
     * 该map存放所有原始数据类型,包括:基本类型,基本数组类型,void类类型;
     * key:类类型名称-value:原始数据类类型
     */
    private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);

    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

    private static final Map<String, Class<?>> commonClassCache = new HashMap<>(32);

    static {
        /**
         * 将基本类型以 key:封装类类型-value:基本类类型 的形式放入primitiveWrapperTypeMap;
         */
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);


        primitiveWrapperTypeMap.forEach((key, value) -> {
            primitiveTypeToWrapperMap.put(value, key);
            registerCommonClasses(key);
        });
        // 该变量将用于存放所有原始类类型,包括:基本类型,基本数组类型,void类类型;
        Set<Class<?>> primitiveTypes = new HashSet<>(32);
        primitiveTypes.addAll(primitiveWrapperTypeMap.values());
        primitiveTypes.addAll(Arrays.asList(new Class<?>[]{
                boolean[].class, byte[].class, char[].class, double[].class,
                float[].class, int[].class, long[].class, short[].class
        }));
        primitiveTypes.add(void.class);
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
        }

        // 将所有基本封装类型的数组类类型放入commonClassCache;
        registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
                Float[].class, Integer[].class, Long[].class, Short[].class);

        registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
                Object.class, Object[].class, Class.class, Class[].class);

        registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
                Error.class, StackTraceElement.class, StackTraceElement[].class);
    }

    /**
     * 将类类型对象放入commonClassCache中
     *
     * @param commonClasses
     */
    private static void registerCommonClasses(Class<?>... commonClasses) {
        for (Class<?> clazz : commonClasses) {
            commonClassCache.put(clazz.getName(), clazz);
        }
    }

    public static Class<?> forName(String name, @Nullable ClassLoader classLoader) {
        // 对象为空则抛出IllegalArgumentException;
        Assert.notNull(name, "name必须不为空");
        return null;
    }

    /**
     * 通过name在primitiveTypeNameMap中获取值;
     *
     * @param name
     * @return
     */
    @Nullable
    public static Class<?> resolvePrimitiveClassName(@Nullable String name) {
        Class<?> result = null;
        // name不为空且长度小于9;
        if (name != null && name.length() <= 8) {
            result = primitiveTypeNameMap.get(name);
        }
        return result;
    }
}
