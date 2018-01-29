package team811.core;

import team811.core.SerializableTypeWrapper.TypeProvider;
import team811.lang.Nullable;
import team811.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @create: 2018-01-28
 * @description:
 */
public class ResolvableType implements Serializable {

    @Nullable
    private final ResolvableType componentType;

    /** 接口包含方法：获取类型名 */
    private final Type type;

    @Nullable
    private final TypeProvider typeProvider;

    @Nullable
    private final VariableResolver variableResolver;

    @Nullable
    private final Integer hash;

    /** 类类型 */
    @Nullable
    private Class<?> resolved;

    private ResolvableType(@Nullable Class<?> clazz) {
        this.resolved = (clazz != null ? clazz : Object.class);
        // Class 是 Type 实现类
        this.type = this.resolved;
        this.typeProvider = null;
        this.variableResolver = null;
        this.componentType = null;
        this.hash = null;
    }

    private ResolvableType(Type type, @Nullable TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = null;
        this.hash = calculateHashCode();
        this.resolved = null;
    }

    private int calculateHashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.type);
    }

    interface VariableResolver extends Serializable {

        Object getSource();

        @Nullable
        ResolvableType resolveVariable(TypeVariable<?> variable);

    }
}
