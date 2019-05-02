package team811.context.annotation;

import team811.context.ResourceLoaderAware;
import team811.core.env.Environment;
import team811.core.env.EnvironmentCapable;
import team811.core.env.StandardEnvironment;

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
