package org.lucas.web.server.adapter;

import org.lucas.web.server.reactive.HttpHandler;
import reactor.core.publisher.Mono;

public class HttpWebHandlerAdapter implements HttpHandler {

    @Override
    public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
        if (this.forwardedHeaderTransformer != null) {
            request = this.forwardedHeaderTransformer.apply(request);
        }
        // 1 创建服务交互对象
        ServerWebExchange exchange = createExchange(request, response);

        LogFormatUtils.traceDebug(logger, traceOn ->
                exchange.getLogPrefix() + formatRequest(exchange.getRequest()) +
                        (traceOn ? ", headers=" + formatHeaders(exchange.getRequest().getHeaders()) : ""));
        // 2 这里 getDelegate() 为 DispatcherHandler
        return getDelegate().handle(exchange)
                .doOnSuccess(aVoid -> logResponse(exchange))
                .onErrorResume(ex -> handleUnresolvedError(exchange, ex))
                .then(Mono.defer(response::setComplete));
    }

    /**
     * 创建服务交互对象
     *
     * @param request  客户端请求
     * @param response 客户端响应
     * @return 服务交互对象
     */
    protected ServerWebExchange createExchange(ServerHttpRequest request, ServerHttpResponse response) {
        return new DefaultServerWebExchange(request, response, this.sessionManager,
                getCodecConfigurer(), getLocaleContextResolver(), this.applicationContext);
    }

}
