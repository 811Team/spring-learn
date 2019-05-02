package team811.util.concurrent;

import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * @create: 2018-01-08
 * @description:
 */
public class ListenableFutureTaskTests {

    @Test
    public void success() throws Exception {
        final String s = "Hello World";
        Callable<String> callable = () -> s;

        ListenableFutureTask<String> task = new ListenableFutureTask<>(callable);
        task.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                assertEquals(s, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                fail(ex.getMessage());
            }
        });
        task.run();

        assertSame(s, task.get());
        assertSame(s, task.completable().get());
        task.completable().thenAccept(v -> assertSame(s, v));
    }
}
