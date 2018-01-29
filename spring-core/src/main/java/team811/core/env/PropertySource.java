package team811.core.env;

import team811.util.Assert;
import team811.util.ObjectUtils;

/**
 * @create: 2018-01-29
 * @description:
 */
public abstract class PropertySource<T> {

    protected final String name;

    protected final T source;

    public PropertySource(String name) {
        this(name, (T) new Object());
    }

    /**
     * @param name   名称不能为空，不能包含空字符
     * @param source 不能为空
     */
    public PropertySource(String name, T source) {
        Assert.hasText(name, "Property source name must contain at least one character");
        Assert.notNull(source, "Property source must not be null");
        this.name = name;
        this.source = source;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || (obj instanceof PropertySource &&
                ObjectUtils.nullSafeEquals(this.name, ((PropertySource<?>) obj).name)));
    }

}
