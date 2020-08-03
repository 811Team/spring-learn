package org.lucas.boot.web.reactive.server;

import org.springframework.http.server.reactive.HttpHandler;

@FunctionalInterface
public interface ReactiveWebServerFactory {

    WebServer getWebServer(HttpHandler httpHandler);

}
