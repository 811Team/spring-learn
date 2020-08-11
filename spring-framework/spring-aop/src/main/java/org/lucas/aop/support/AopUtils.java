package org.lucas.aop.support;

import org.lucas.aop.SpringProxy;
import org.lucas.aop.TargetClassAware;
import org.lucas.lang.Nullable;
import org.lucas.util.Assert;
import org.lucas.util.ClassUtils;

public abstract class AopUtils {

    /**
     * 获取被其代理的对象
     *
     * @param candidate
     * @return
     */
    public static Class<?> getTargetClass(Object candidate) {
        Assert.notNull(candidate, "Candidate object must not be null");
        Class<?> result = null;
        if (candidate instanceof TargetClassAware) {
            // 直接获取目标对象
            result = ((TargetClassAware) candidate).getTargetClass();
        }
        if (result == null) {
            // 如果是 Cglib 代理对象，则根据原理获取父类是其被代理的对象，否则获取当前类类型。
            result = (isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass());
        }
        return result;
    }

    /**
     * 判断该对象是否 Spring CGLIB 代理对象
     *
     * @param object 判断对象
     * @return if {@code true} is proxy
     */
    public static boolean isCglibProxy(@Nullable Object object) {
        return (object instanceof SpringProxy &&
                object.getClass().getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR));
    }

}
