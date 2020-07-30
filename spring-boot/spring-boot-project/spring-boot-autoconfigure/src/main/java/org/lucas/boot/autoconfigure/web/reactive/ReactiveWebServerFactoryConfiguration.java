package org.lucas.boot.autoconfigure.web.reactive;

import java.util.stream.Collectors;

abstract class ReactiveWebServerFactoryConfiguration {

    /**
     * 注入 NettyReactiveWebServerFactory 注入容器
     * <p>
     * 如果当前容器上下文中不存在 ReactiveWebServerFactory 的实
     * 例，并且 classpath 下存在 HttpServer 的 class 文件，则
     * 说明当前环境为 Reactive 环境，则注入 NettyReactiveWebServerFactory
     * 到容器。
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({HttpServer.class})
    static class EmbeddedNetty {

        @Bean
        @ConditionalOnMissingBean
        ReactorResourceFactory reactorServerResourceFactory() {
            return new ReactorResourceFactory();
        }

        @Bean
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory(ReactorResourceFactory resourceFactory,
                                                                    ObjectProvider<NettyRouteProvider> routes, ObjectProvider<NettyServerCustomizer> serverCustomizers) {
            NettyReactiveWebServerFactory serverFactory = new NettyReactiveWebServerFactory();
            serverFactory.setResourceFactory(resourceFactory);
            routes.orderedStream().forEach(serverFactory::addRouteProviders);
            serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
            return serverFactory;
        }

    }

    /**
     * 注入 TomcatReactiveWebServerFactory 实例
     * <p>
     * 如果当前容器上下文中不存在 ReactiveWebServerFactory 的实例，并且
     * classpath 下存在 org.apache.catalina.startup.Tomcat 的 class 文
     * 件，则说明当前环境为 Servlet 环境，并且 Servlet 容器为 Tomcat，则将
     * TomcatReactiveWebServerFactory 实例注入容器。
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({org.apache.catalina.startup.Tomcat.class})
    static class EmbeddedTomcat {

        @Bean
        TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory(
                ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
                ObjectProvider<TomcatContextCustomizer> contextCustomizers,
                ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
            TomcatReactiveWebServerFactory factory = new TomcatReactiveWebServerFactory();
            factory.getTomcatConnectorCustomizers()
                    .addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
            factory.getTomcatContextCustomizers()
                    .addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
            factory.getTomcatProtocolHandlerCustomizers()
                    .addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
            return factory;
        }

    }

    /**
     * 注入 JettyReactiveWebServerFactory 实例
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({ org.eclipse.jetty.server.Server.class, ServletHolder.class })
    static class EmbeddedJetty {

        @Bean
        @ConditionalOnMissingBean
        JettyResourceFactory jettyServerResourceFactory() {
            return new JettyResourceFactory();
        }

        @Bean
        JettyReactiveWebServerFactory jettyReactiveWebServerFactory(JettyResourceFactory resourceFactory,
                                                                    ObjectProvider<JettyServerCustomizer> serverCustomizers) {
            JettyReactiveWebServerFactory serverFactory = new JettyReactiveWebServerFactory();
            serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
            serverFactory.setResourceFactory(resourceFactory);
            return serverFactory;
        }

    }

    /**
     * 注入 UndertowReactiveWebServerFactory 实例
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveWebServerFactory.class)
    @ConditionalOnClass({Undertow.class})
    static class EmbeddedUndertow {

        @Bean
        UndertowReactiveWebServerFactory undertowReactiveWebServerFactory(
                ObjectProvider<UndertowBuilderCustomizer> builderCustomizers) {
            UndertowReactiveWebServerFactory factory = new UndertowReactiveWebServerFactory();
            factory.getBuilderCustomizers().addAll(builderCustomizers.orderedStream().collect(Collectors.toList()));
            return factory;
        }

    }

}
