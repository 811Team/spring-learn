package org.lucas.servlet;

import java.util.Properties;

/**
 *  @author  lihailen
 *  @date 2017-07-02
 *
 * 此源码来自己于Spring 4.1.5
 *
 * HTTP请求处理器/控制器的中央分发器，例如web UI 控制器或是基于HTTP远程服务出口商。调度注册处理器用于处理一个web请求，
 * 提供便利的映射与异常处理工具。
 * <p>
 *     这个servlet有很好的扩展性:
 * </p>
 */

@SuppressWarnings("serial")
public class DispatcherServlet extends FrameworkServlet {
    /** 已知MultipartResolver对象在bean工厂命名空间的名字 */
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

    /** 已知LocaleResolver对象在bean工厂命名空间的名字 */
    public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";

    /** 已知ThemeResolver对象在bean工厂命名空间的名字 */
    private static final String HTEME_RESOLVER_BEAN_NAME = "themeResolver";

    private static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

    private static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

    private static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";

    private static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";

    private static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    private static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashManager";

    /** 持有当前web应用上下文的请求属性 */
    private static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /** 持有当前LocaleResolver的请求属性，根据视图获取*/
    private static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";

    /** 持有当前ThemeResolver的请求属性，根据视图获取 */
    private static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";

    /** 持有当前ThemeSource的请求属性，根据视图获取 */
    private static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";

    /** 持有一个只读请求属性的名字 */
    public static final String INPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".INPUT_FLASH_MAP";

    public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".OUTPUT_FLASH_MAP";

    public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";

    public static final String EXCEPTION_ATTRIBUTE = DispatcherServlet.class.getName() + ".EXCEPTION";

    /** 日志类别，当没有映射处理程序发现一个请求时 */
    public static final String PAGE_NOT_FOUND_LOG_CAGEGORY = "org.springframework.web.servlet.PageNotFound";

    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    // 源码中此处并没有写出类全名
    protected static final org.apache.commons.logging.Log pageNotFoundLogger = LogFactory.getLog(PAGE_NOT_FOUND_LOG_CATEGORY);

    private static final Properties defaultStrategies;
}
