package org.lucas.retry.support;

import org.junit.Test;
import org.lucas.retry.RetryCallback;
import org.lucas.retry.RetryContext;
import org.lucas.retry.policy.SimpleRetryPolicy;

import static org.junit.Assert.assertEquals;

public class RetryTemplateTests {

    RetryContext context;

    int count = 0;

    @Test
    public void testSuccessfulRetry() throws Throwable {
        for (int x = 1; x <= 10; x++) {
            MockRetryCallback callback = new MockRetryCallback();
            callback.setAttemptsBeforeSuccess(x);
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(x));
            retryTemplate.execute(callback);
            assertEquals(x, callback.attempts);
        }
    }

    private static class MockRetryCallback implements RetryCallback<Object, Exception> {

        private int attempts;

        /**
         * 最大尝试次数
         */
        private int attemptsBeforeSuccess;

        private Exception exceptionToThrow = new Exception();

        @Override
        public Object doWithRetry(RetryContext status) throws Exception {
            this.attempts++;
            if (this.attempts < this.attemptsBeforeSuccess) {
                throw this.exceptionToThrow;
            }
            return null;
        }

        public void setAttemptsBeforeSuccess(int attemptsBeforeSuccess) {
            this.attemptsBeforeSuccess = attemptsBeforeSuccess;
        }

        public void setExceptionToThrow(Exception exceptionToThrow) {
            this.exceptionToThrow = exceptionToThrow;
        }

    }

}
