package com.example.batch.utils;

import java.util.function.Consumer;

public class TimeCounter implements AutoCloseable {

    private Consumer<Long> resultTimeConsumer;
    private final long start;

    public TimeCounter() {
        this.start = System.currentTimeMillis();
        this.resultTimeConsumer = delta -> System.out.println("Elapsed " +  delta + "ms");
    }

    public TimeCounter(Consumer<Long> resultTimeConsumer) {
        this.start = System.currentTimeMillis();
        this.resultTimeConsumer = resultTimeConsumer;
    }

    @Override
    public void close() {
        long end = System.currentTimeMillis();
        if (resultTimeConsumer != null) {
            resultTimeConsumer.accept(end - start);
        }
    }

}
