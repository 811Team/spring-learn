package org.lucas.context.annotation;

import org.lucas.context.ResourceLoaderAware;
import org.lucas.core.env.Environment;
import org.lucas.core.env.EnvironmentCapable;
import org.lucas.core.env.StandardEnvironment;

/**
 * @create: 2018-01-26
 * @description:
 */
public class ClassPathScanningCandidateComponentProvider implements EnvironmentCapable, ResourceLoaderAware {
    protected ClassPathScanningCandidateComponentProvider() {
    }

    public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
        this(useDefaultFilters, new StandardEnvironment());
    }

    public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters, Environment environment) {
        if (useDefaultFilters) {
            registerDefaultFilters();
        }
        setEnvironment(environment);
        setResourceLoader(null);
    }
}
