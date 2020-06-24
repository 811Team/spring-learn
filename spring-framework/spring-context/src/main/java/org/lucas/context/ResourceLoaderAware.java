package org.lucas.context;

import org.lucas.beans.factory.Aware;
import org.lucas.core.io.ResourceLoader;

/**
 * @create: 2018-01-26
 */
public interface ResourceLoaderAware extends Aware {

    void setResourceLoader(ResourceLoader resourceLoader);

}
