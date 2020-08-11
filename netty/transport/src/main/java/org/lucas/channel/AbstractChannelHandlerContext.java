package org.lucas.channel;

abstract class AbstractChannelHandlerContext implements ChannelHandlerContext {

    private void write(Object msg, boolean flush, ChannelPromise promise) {
        ObjectUtil.checkNotNull(msg, "msg");
        try {
            if (isNotValidPromise(promise, true)) {
                ReferenceCountUtil.release(msg);
                // cancelled
                return;
            }
        } catch (RuntimeException e) {
            ReferenceCountUtil.release(msg);
            throw e;
        }

        final AbstractChannelHandlerContext next = findContextOutbound(flush ?
                (MASK_WRITE | MASK_FLUSH) : MASK_WRITE);
        final Object m = pipeline.touch(msg, next);
        EventExecutor executor = next.executor();
        // 1 如果调用的线程是 IO 线程，则在 IO 线程上执行写入
        if (executor.inEventLoop()) {
            if (flush) {
                next.invokeWriteAndFlush(m, promise);
            } else {
                next.invokeWrite(m, promise);
            }
        } else {
            // 2 如果调用的不是 IO 线程，则会把写入请求封装为 WriteTask 并投递到
            // 与其对应的 NioEventLoop 中的队列里面，然后等其对应的 NioEventLoop 中
            // 的线程轮询连接套接字的读写事件时捎带从队列里面取出来并执行。
            final WriteTask task = WriteTask.newInstance(next, m, promise, flush);
            if (!safeExecute(executor, task, promise, m, !flush)) {
                task.cancel();
            }
        }
    }
}
