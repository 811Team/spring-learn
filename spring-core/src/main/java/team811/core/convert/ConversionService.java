package team811.core.convert;

import team811.lang.Nullable;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface ConversionService {

    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
}
