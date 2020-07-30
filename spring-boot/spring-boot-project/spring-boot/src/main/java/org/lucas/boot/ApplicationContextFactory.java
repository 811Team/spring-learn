package org.lucas.boot;

import org.lucas.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;

public interface ApplicationContextFactory {

    ApplicationContextFactory DEFAULT = (webApplicationType) -> {
        try {
            // a 环境类型
            switch (webApplicationType) {
                // a.1 Web Servlet 环境
                case SERVLET:
                    return new AnnotationConfigServletWebServerApplicationContext();
                // a.2 Web Reactive 环境
                case REACTIVE:
                    return new AnnotationConfigReactiveWebServerApplicationContext();
                default:
                    // 非 Web 环境
                    return new AnnotationConfigApplicationContext();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Unable create a default ApplicationContext instance, "
                    + "you may need a custom ApplicationContextFactory", ex);
        }
    };

    ConfigurableApplicationContext create(WebApplicationType webApplicationType);

}
