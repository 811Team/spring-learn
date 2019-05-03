package org.lucas.core.type.filter;

import org.lucas.core.type.classreading.MetadataReader;
import org.lucas.core.type.classreading.MetadataReaderFactory;

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
