package team811.core.type;

import team811.lang.Nullable;
import team811.util.MultiValueMap;

import java.util.Map;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface AnnotatedTypeMetadata {
    /**
     * 该元素是否有注解
     */
    boolean isAnnotated(String annotationName);

    @Nullable
    Map<String, Object> getAnnotationAttributes(String annotationName);

    @Nullable
    Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString);

    @Nullable
    MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName);

    @Nullable
    MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString);
}
