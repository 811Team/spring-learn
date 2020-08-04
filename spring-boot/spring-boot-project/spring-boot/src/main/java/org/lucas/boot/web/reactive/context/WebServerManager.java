package org.lucas.boot.web.reactive.context;

import org.lucas.boot.web.reactive.server.ReactiveWebServerFactory;
import org.lucas.boot.web.server.WebServer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

class WebServerManager {

    private final ReactiveWebServerApplicationContext applicationContext;

    /**
     * 延迟的{@link HttpHandler}，不会太早初始化东西。
     */
    private final DelayedInitializationHttpHandler handler;

    /**
     * Web服务器
     */
    private final WebServer webServer;

    WebServerManager(ReactiveWebServerApplicationContext applicationContext, ReactiveWebServerFactory factory,
                     Supplier<HttpHandler> handlerSupplier, boolean lazyInit) {
        this.applicationContext = applicationContext;
        Assert.notNull(factory, "Factory must not be null");
        // 延迟加载 handler
        this.handler = new DelayedInitializationHttpHandler(handlerSupplier, lazyInit);
        // 通过 bean 工厂获取 server
        this.webServer = factory.getWebServer(this.handler);
    }

    /**
     * 启动 Web 服务器
     */
    void start() {
        // 加载上下文 HttpHandler
        this.handler.initializeHandler();
        // 启动服务
        this.webServer.start();
        this.applicationContext
                .publishEvent(new ReactiveWebServerInitializedEvent(this.webServer, this.applicationContext));
    }

    /**
     * 停止 Web 服务器
     */
    void stop() {
        this.webServer.stop();
    }

    static final class DelayedInitializationHttpHandler implements HttpHandler {

        private final Supplier<HttpHandler> handlerSupplier;

        private final boolean lazyInit;

        /**
         * 初始化为抛出 IllegalStateException 异常
         */
        private volatile HttpHandler delegate = this::handleUninitialized;

        private DelayedInitializationHttpHandler(Supplier<HttpHandler> handlerSupplier, boolean lazyInit) {
            this.handlerSupplier = handlerSupplier;
            this.lazyInit = lazyInit;
        }

        void initializeHandler() {
            this.delegate = this.lazyInit ? new LazyHttpHandler(this.handlerSupplier) : this.handlerSupplier.get();
        }

        /**
         * 始化异常
         *
         * @throws IllegalStateException
         */
        private Mono<Void> handleUninitialized(ServerHttpRequest request, ServerHttpResponse response) {
            throw new IllegalStateException("The HttpHandler has not yet been initialized");
        }

        @Override
        public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
            return this.delegate.handle(request, response);
        }

    }

    /**
     * {@link HttpHandler} that initializes its delegate on first request.
     */
    private static final class LazyHttpHandler implements HttpHandler {

        private final Mono<HttpHandler> delegate;

        /**
         * 延迟加载上下文 HttpHandler
         */
        private LazyHttpHandler(Supplier<HttpHandler> handlerSupplier) {
            this.delegate = Mono.fromSupplier(handlerSupplier);
        }

        @Override
        public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
            return this.delegate.flatMap((handler) -> handler.handle(request, response));
        }

    }

}
