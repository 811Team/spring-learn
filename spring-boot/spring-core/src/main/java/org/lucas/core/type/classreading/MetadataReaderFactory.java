package org.lucas.core.type.classreading;

import org.lucas.core.io.Resource;

import java.io.IOException;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface MetadataReaderFactory {

    MetadataReader getMetadataReader(String className) throws IOException;

    MetadataReader getMetadataReader(Resource resource) throws IOException;
}
