package com.example.batch.flux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluxBatchLoadService {

    private FluxBatchLoadService() {}

    public static <T, ST> Stream<T> loadAll(Function<ST, Stream<T>> producer,
                                            Function<T, ST> next,
                                            ST st) {
        return Flux.<List<T>, ST>generate(
                        () -> st,
                        (startingAfter, sink) -> {
                            List<T> beans = producer.apply(startingAfter).collect(Collectors.toList());

                            if (beans.isEmpty()) {
                                sink.complete();
                                return null;

                            } else {
                                sink.next(beans);
                                return next.apply(beans.get(beans.size() - 1));
                            }
                        }).flatMap(Flux::<T>fromIterable, 1, 1)
                .toStream(1);
    }

}
