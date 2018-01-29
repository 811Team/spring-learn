package team811.core.type;

import team811.lang.Nullable;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface ClassMetadata {
    /**
     * 返回类名
     */
    String getClassName();

    /**
     * 该类是否是一个接口
     */
    boolean isInterface();

    /**
     * 该类是否是一个抽象类
     */
    boolean isAbstract();

    /**
     * 返回类是否是一个具体实现类
     */
    boolean isConcrete();

    /**
     * 返回该类是否被标记为 "final"
     */
    boolean isFinal();

    boolean isIndependent();

    boolean hasEnclosingClass();

    @Nullable
    String getEnclosingClassName();

    /**
     * 底层是否是父类
     */
    boolean hasSuperClass();

    @Nullable
    String getSuperClassName();

    /**
     * 返回该类的所有接口类名
     */
    String[] getInterfaceNames();

    String[] getMemberClassNames();
}
