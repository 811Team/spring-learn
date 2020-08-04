package org.lucas.web.reactive;

import org.lucas.context.ApplicationContextAware;
import org.lucas.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DispatcherHandler implements WebHandler, ApplicationContextAware {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {
        if (this.handlerMappings == null) {
            return createNotFoundError();
        }
        return Flux.fromIterable(this.handlerMappings)
                .concatMap(mapping -> mapping.getHandler(exchange))
                .next()
                .switchIfEmpty(createNotFoundError())
                .flatMap(handler -> invokeHandler(exchange, handler))
                .flatMap(result -> handleResult(exchange, result));
    }
}
