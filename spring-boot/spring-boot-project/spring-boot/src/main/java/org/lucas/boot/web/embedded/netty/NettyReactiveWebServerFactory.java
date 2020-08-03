package org.lucas.boot.web.embedded.netty;

import org.lucas.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.util.Assert;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

import java.net.InetSocketAddress;

public class NettyReactiveWebServerFactory extends AbstractReactiveWebServerFactory {

    /**
     * 获取 Web 服务器
     *
     * @param httpHandler
     * @return
     */
    @Override
    public WebServer getWebServer(HttpHandler httpHandler) {
        // 1 创建 HttpServer.
        HttpServer httpServer = createHttpServer();
        // 2 创建与Netty对应的适配器类ReactorHttpHandlerAdapter。
        ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        // 3 创建 NettyWebServer 的实例，其包装了适配器和 HttpServer 实例。
        NettyWebServer webServer = new NettyWebServer(httpServer, handlerAdapter, this.lifecycleTimeout, getShutdown());
        webServer.setRouteProviders(this.routeProviders);
        return webServer;
    }

    /**
     * 创建 HttpServer.
     *
     * @return HttpServer.
     */
    private HttpServer createHttpServer() {
        // 1 创建 HttpServer。
        HttpServer server = HttpServer.create();
        if (this.resourceFactory != null) {
            LoopResources resources = this.resourceFactory.getLoopResources();
            Assert.notNull(resources, "No LoopResources: is ReactorResourceFactory not initialized yet?");
            server = server.runOn(resources).bindAddress(this::getListenAddress);
        } else {
            server = server.bindAddress(this::getListenAddress);
        }
        if (getSsl() != null && getSsl().isEnabled()) {
            SslServerCustomizer sslServerCustomizer = new SslServerCustomizer(getSsl(), getHttp2(),
                    getSslStoreProvider());
            server = sslServerCustomizer.apply(server);
        }
        if (getCompression() != null && getCompression().getEnabled()) {
            CompressionCustomizer compressionCustomizer = new CompressionCustomizer(getCompression());
            server = compressionCustomizer.apply(server);
        }
        server = server.protocol(listProtocols()).forwarded(this.useForwardHeaders);
        return applyCustomizers(server);
    }

    /**
     * @return 获取绑定地址
     */
    private InetSocketAddress getListenAddress() {
        if (getAddress() != null) {
            return new InetSocketAddress(getAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

}
