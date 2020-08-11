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
        // 1 查找对应的 controller 进行处理
        // 1.1 获取所有的处理器映射
        return Flux.fromIterable(this.handlerMappings)
                // 1.2 转换映射，获取处理器
                .concatMap(mapping -> mapping.getHandler(exchange))
                // 1.3 获取第一个处理器
                .next()
                // 1.4 如果不存在处理器，则创建一个错误信息作为元素
                .switchIfEmpty(createNotFoundError())
                // 1.5 如果有处理器，则调用 invokeHandler 进行处理
                .flatMap(handler -> invokeHandler(exchange, handler))
                // 1.6 处理处理器处理的结果，交由 handleResult 写回响应对象。
                .flatMap(result -> handleResult(exchange, result));
    }
}
