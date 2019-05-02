package team811.core.env;

import team811.lang.Nullable;

/**
 * @create: 2018-01-29
 * @description:
 */
public interface PropertySources extends Iterable<PropertySource<?>> {

    boolean contains(String name);

    @Nullable
    PropertySource<?> get(String name);
}
