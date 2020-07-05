package main.java.com.company.analysis;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe message counter
 *
 * @author Ivana Salmanić
 */

public class AtomicMessageCounter {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int incrementMessageCounterAndReturn() {
        return counter.incrementAndGet();
    }

    public static int getMessageCounter() {
        return counter.get();
    }
}
