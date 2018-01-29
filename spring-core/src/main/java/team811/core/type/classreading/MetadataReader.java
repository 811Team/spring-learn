package team811.core.type.classreading;

import team811.core.io.Resource;
import team811.core.type.AnnotationMetadata;
import team811.core.type.ClassMetadata;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface MetadataReader {
    /**
     * 获取元数据的 {@code Resource} 对象
     *
     * @return {@code Resource}
     */
    Resource getResource();

    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();

}
