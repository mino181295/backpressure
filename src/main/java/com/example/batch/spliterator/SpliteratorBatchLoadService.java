package com.example.batch.spliterator;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SpliteratorBatchLoadService {

    private SpliteratorBatchLoadService() {
    }

    public static <K, T> Stream<T> loadAll(Function<K, Stream<T>> mainFunction,
                                           Function<T, K> keyExtractor) {
        return loadAll(mainFunction, keyExtractor, null);
    }

    public static <K, T> Stream<T> loadAll(Function<K, Stream<T>> mainFunction,
                                           Function<T, K> keyExtractor,
                                           K startingAfter) {
        return StreamSupport
                .stream(new BatchSpliterator<>(mainFunction, keyExtractor, startingAfter), false)
                .flatMap(List::stream);
    }

}
