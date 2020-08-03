package org.lucas.boot.web.reactive.context;

import org.lucas.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.Assert;

import java.util.function.Supplier;

class WebServerManager {

    /**
     * Web服务器
     */
    private final WebServer webServer;

    WebServerManager(ReactiveWebServerApplicationContext applicationContext, ReactiveWebServerFactory factory,
                     Supplier<HttpHandler> handlerSupplier, boolean lazyInit) {
        this.applicationContext = applicationContext;
        Assert.notNull(factory, "Factory must not be null");
        this.handler = new DelayedInitializationHttpHandler(handlerSupplier, lazyInit);
        // 通过 bean 工厂获取 server
        this.webServer = factory.getWebServer(this.handler);
    }

}
