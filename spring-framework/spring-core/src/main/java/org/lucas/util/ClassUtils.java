package org.lucas.util;

import org.lucas.lang.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 知识点:
 * <p>
 * IdentityHashMap: key值可以重复的map对象,取值时,通过判断 obj1==obj2 判断是否为重复 Key，而不是普通HashMap的equals方式判断;
 */
public abstract class ClassUtils {

    /**
     * 数组后缀符号
     */
    public static final String ARRAY_SUFFIX = "[]";

    /**
     * 内部数组前缀名
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[";

    /**
     * 非基础数组类型前缀
     */
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

    /**
     * 包的分隔符: '.'
     */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * 内部类分隔符
     */
    private static final char INNER_CLASS_SEPARATOR = '$';

    /**
     * CGLIB分隔符
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

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

    /**
     * 根据类全名加载
     *
     * @param name        类全名
     * @param classLoader 类加载器
     * @return
     * @throws ClassNotFoundException
     * @throws LinkageError
     */
    public static Class<?> forName(String name, @Nullable ClassLoader classLoader)
            throws ClassNotFoundException, LinkageError {
        // 对象为空则抛出IllegalArgumentException;
        Assert.notNull(name, "name必须不为空");

        // 尝试在 primitiveWrapperTypeMap 中获取
        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz == null) {
            clazz = commonClassCache.get(name);
        }

        if (clazz != null) {
            return clazz;
        }
        // 数组类类型
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = forName(elementClassName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }

        // 非基础数组类型前缀
        if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
            String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }
        // 内部数组
        if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
            String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
            Class<?> elementClass = forName(elementName, classLoader);
            return Array.newInstance(elementClass, 0).getClass();
        }
        // 类加载器
        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = getDefaultClassLoader();
        }
        try {
            return (clToUse != null ? clToUse.loadClass(name) : Class.forName(name));
        } catch (ClassNotFoundException ex) {
            // 尝试内部类加载，在类名加上前缀 "$"
            int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
            if (lastDotIndex != -1) {
                String innerClassName =
                        name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
                try {
                    return (clToUse != null ? clToUse.loadClass(innerClassName) : Class.forName(innerClassName));
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
            throw ex;
        }
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

    /**
     * 获取类名(不含包名)
     *
     * @param clazz
     * @return
     */
    public static String getShortName(Class<?> clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    public static String getShortName(String className) {
        Assert.hasLength(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }

    public static String getQualifiedName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.getTypeName();
    }

    /**
     * 获取默认类加载器
     *
     * @return 顺序：当前线程类加载器 <- 当前类的加载器 <- 系统类加载器
     */
    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }


    public static Method getMostSpecificMethod(Method method, @Nullable Class<?> targetClass) {
        // 目标对象和目标方法是否可以重写
        if (targetClass != null && targetClass != method.getDeclaringClass() && isOverridable(method, targetClass)) {
            try {
                // public 方法判断
                if (Modifier.isPublic(method.getModifiers())) {
                    try {
                        // 获取目标对象的重写方法
                        return targetClass.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException ex) {
                        return method;
                    }
                } else {
                    Method specificMethod =
                            ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
                    return (specificMethod != null ? specificMethod : method);
                }
            } catch (SecurityException ex) {
                // Security settings are disallowing reflective access; fall back to 'method' below.
            }
        }
        return method;
    }

    /**
     * 通过类型获取包名
     *
     * @param clazz 类型
     * @return 包名
     */
    public static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    /**
     * 通过类全名获取包名
     *
     * @param fqClassName 类全名
     * @return 包名
     */
    public static String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    /**
     * 判断方法或对象的方法是否可以重写
     *
     * @param method      方法
     * @param targetClass 目标对象
     * @return {@code true} 可以重写
     */
    private static boolean isOverridable(Method method, @Nullable Class<?> targetClass) {
        // 判断方法是否 private，如果是则不是可以重写的方法
        if (Modifier.isPrivate(method.getModifiers())) {
            return false;
        }
        // 判断方法是否 public 或 protected，如果是则可以重写
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            return true;
        }
        // 该方法申明的对象跟目标对象是否在同一个包下，如果是，则可以重写
        return (targetClass == null ||
                getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
    }


}
