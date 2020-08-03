package org.lucas.boot;

import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class SpringApplication {

    private Set<Class<?>> primarySources;

    private ResourceLoader resourceLoader;

    /**
     * 环境类型
     */
    private WebApplicationType webApplicationType;

    private ApplicationContextFactory applicationContextFactory = ApplicationContextFactory.DEFAULT;

    public SpringApplication(Class<?>... primarySources) {
        this(null, primarySources);
    }

    public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        this.resourceLoader = resourceLoader;
        Assert.notNull(primarySources, "PrimarySources must not be null");
        this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
        // 判断当前环境类型
        this.webApplicationType = WebApplicationType.deduceFromClasspath();
        setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
        setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
        this.mainApplicationClass = deduceMainApplicationClass();
    }

    protected ConfigurableApplicationContext createApplicationContext() {
        return this.applicationContextFactory.create(this.webApplicationType);
    }

}
