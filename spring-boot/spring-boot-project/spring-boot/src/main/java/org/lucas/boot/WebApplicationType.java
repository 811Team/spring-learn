package org.lucas.boot;

import org.springframework.util.ClassUtils;

public enum WebApplicationType {

    /**
     * 非 Web 服务
     */
    NONE,

    /**
     * Servlet Web 服务
     */
    SERVLET,

    /**
     * Reactive Web 服务
     */
    REACTIVE;

    /**
     * Servlet 所需要的类
     */
    private static final String[] SERVLET_INDICATOR_CLASSES = {"javax.servlet.Servlet",
            "org.springframework.web.context.ConfigurableWebApplicationContext"};

    /**
     * spring mvc 分派器
     */
    private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    /**
     * Reactive 分派器
     */
    private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

    /**
     * Jersey 项目容器类
     */
    private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

    /**
     * @return 判断环境类型
     */
    static WebApplicationType deduceFromClasspath() {
        // 1 Reactive 环境
        // 1.1 存在 org.springframework.web.reactive.DispatcherHandler 类
        // 1.2 不存在 org.springframework.web.servlet.DispatcherServlet 类
        // 1.3 不存在 org.glassfish.jersey.servlet.ServletContainer 类
        if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
                && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        // 2 非 web 环境
        // 2.1 不存在 javax.servlet.Servlet
        // 2.2 不存在 org.springframework.web.context.ConfigurableWebApplicationContext
        for (String className : SERVLET_INDICATOR_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        // 3 默认为 web Servlet 服务
        return WebApplicationType.SERVLET;
    }

}
