package org.lucas.boot.web.reactive.context;

public class ReactiveWebServerApplicationContext {

    private void createWebServer() {
        WebServerManager serverManager = this.serverManager;
        if (serverManager == null) {
            // 从 Bean 工厂中获取 ReactiveWebServerFactory 类型的 Bean 实例的名字
            String webServerFactoryBeanName = getWebServerFactoryBeanName();
            ReactiveWebServerFactory webServerFactory = getWebServerFactory(webServerFactoryBeanName);
            boolean lazyInit = getBeanFactory().getBeanDefinition(webServerFactoryBeanName).isLazyInit();
            this.serverManager = new WebServerManager(this, webServerFactory, this::getHttpHandler, lazyInit);
            getBeanFactory().registerSingleton("webServerGracefulShutdown",
                    new WebServerGracefulShutdownLifecycle(this.serverManager));
            getBeanFactory().registerSingleton("webServerStartStop",
                    new WebServerStartStopLifecycle(this.serverManager));
        }
        initPropertySources();
    }

    /**
     * 从 Bean 工厂中获取 ReactiveWebServerFactory 类型的 Bean 实例的名字
     */
    protected String getWebServerFactoryBeanName() {
        // 1 从 Bean 工厂中获取 ReactiveWebServerFactory 类型的 Bean 实例的名字
        String[] beanNames = getBeanFactory().getBeanNamesForType(ReactiveWebServerFactory.class);
        // 2 不存在则抛出异常
        if (beanNames.length == 0) {
            throw new ApplicationContextException(
                    "Unable to start ReactiveWebApplicationContext due to missing ReactiveWebServerFactory bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Unable to start ReactiveWebApplicationContext due to multiple "
                    + "ReactiveWebServerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        // 3 存在则获取第一个实例名称。
        return beanNames[0];
    }

}
