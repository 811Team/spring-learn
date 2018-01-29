package team811.context;

import team811.beans.factory.Aware;
import team811.core.io.ResourceLoader;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface ResourceLoaderAware extends Aware {

    void setResourceLoader(ResourceLoader resourceLoader);

}
