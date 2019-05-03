package org.lucas.core.type.classreading;

import org.lucas.core.type.AnnotationMetadata;
import org.lucas.core.type.ClassMetadata;
import org.lucas.core.io.Resource;

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
