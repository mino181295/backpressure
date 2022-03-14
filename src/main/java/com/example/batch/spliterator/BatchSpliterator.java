package com.example.batch.spliterator;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BatchSpliterator<K, T> implements Spliterator<List<T>> {

    private K offset;
    private long estimateSize = 0;

    private final Function<K, Stream<T>> loader;
    private final Function<T, K> keyExtractor;

    public BatchSpliterator(Function<K, Stream<T>> loader, Function<T, K> keyExtractor) {
        this(loader, keyExtractor, null);
    }

    public BatchSpliterator(Function<K, Stream<T>> loader, Function<T, K> keyExtractor, K offset) {
        this.offset = offset;
        this.loader = loader;
        this.keyExtractor = keyExtractor;
    }

    @Override
    public boolean tryAdvance(Consumer<? super List<T>> action) {
        List<T> result = getList(loader, offset);
        int resultSize;
        if ((resultSize = result.size()) == 0) {
            return false; // Last result is reached, stop the Spliterator
        }
        T lastElement = result.get(result.size() - 1);
        offset = keyExtractor.apply(lastElement);
        estimateSize += resultSize;
        action.accept(result);
        return true;
    }

    private List<T> getList(Function<K, Stream<T>> loader, K startingAfter) {
        return loader.apply(offset).collect(Collectors.toList());
    }

    @Override
    public Spliterator<List<T>> trySplit() {
        return null; // Should return non-null values if you want to use parallel streams
    }

    @Override
    public long estimateSize() {
        return estimateSize; // The size is unknown, so we return Long.MAX_VALUE
    }

    /*
     * Must be ordered, otherwise elements are requested from multiple Lists,
     * which this Spliterator does not support.
     * Other characteristics depend on the implementation of List.elements
     */
    @Override
    public int characteristics() {
        return ORDERED | IMMUTABLE;
    }
}