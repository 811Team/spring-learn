package org.lucas.aop;

public interface ClassFilter {

    /**
     * 切点目标类
     */
    boolean matches(Class<?> clazz);


    /**
     * 匹配所有类的 TrueClassFilter 的一个普通实例
     */
    ClassFilter TRUE = TrueClassFilter.INSTANCE;

}
