package team811.core.type.classreading;

import team811.core.io.Resource;

import java.io.IOException;

/**
 * @create: 2018-01-28
 * @description:
 */
public interface MetadataReaderFactory {

    MetadataReader getMetadataReader(String className) throws IOException;

    MetadataReader getMetadataReader(Resource resource) throws IOException;
}
