package org.lucas.retry.backoff;

public class NoBackOffPolicy extends StatelessBackOffPolicy {

    @Override
    protected void doBackOff() throws BackOffInterruptedException {
    }

    @Override
    public String toString() {
        return "NoBackOffPolicy []";
    }

}
