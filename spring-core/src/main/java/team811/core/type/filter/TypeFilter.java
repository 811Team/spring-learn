package team811.core.type.filter;

import team811.core.type.classreading.MetadataReader;
import team811.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * @create: 2018-01-26
 * @description:
 */
@FunctionalInterface
public interface TypeFilter {

    boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException;

}
