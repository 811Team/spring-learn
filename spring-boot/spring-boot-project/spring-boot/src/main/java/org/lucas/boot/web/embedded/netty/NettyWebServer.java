package org.lucas.boot.web.embedded.netty;

import org.lucas.boot.web.server.WebServer;
import reactor.netty.ChannelBindException;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class NettyWebServer implements WebServer {

    private volatile DisposableServer disposableServer;

    @Override
    public void start() throws WebServerException {
        if (this.disposableServer == null) {
            try {
                this.disposableServer = startHttpServer();
            } catch (Exception ex) {
                PortInUseException.ifCausedBy(ex, ChannelBindException.class, (bindException) -> {
                    if (!isPermissionDenied(bindException.getCause())) {
                        throw new PortInUseException(bindException.localPort(), ex);
                    }
                });
                throw new WebServerException("Unable to start Netty", ex);
            }
            logger.info("Netty started on port(s): " + getPort());
            // 开启 Daemon 等待服务终止
            startDaemonAwaitThread(this.disposableServer);
        }
    }

    private DisposableServer startHttpServer() {
        HttpServer server = this.httpServer;
        if (this.routeProviders.isEmpty()) {
            server = server.handle(this.handler);
        } else {
            server = server.route(this::applyRouteProviders);
        }
        if (this.lifecycleTimeout != null) {
            return server.bindNow(this.lifecycleTimeout);
        }
        return server.bindNow();
    }

    private void startDaemonAwaitThread(DisposableServer disposableServer) {
        // 开启一个线程
        // 之所以开启一个线程来启用服务器，是因为这样不会阻塞调用线程，导致整个应用阻塞。
        Thread awaitThread = new Thread("server") {
            @Override
            public void run() {
                // 同步阻塞服务器停止
                disposableServer.onDispose().block();
            }
        };
        // 设置线程 Daemon，并启动
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

}
