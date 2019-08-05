package org.lucas.core.type;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {

    /**
     * 获取方法名
     */
    String getMethodName();

    String getDeclaringClassName();

    String getReturnTypeName();

    boolean isAbstract();

    boolean isStatic();

    boolean isFinal();

    boolean isOverridable();
}
