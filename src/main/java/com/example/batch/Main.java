package com.example.batch;

import com.example.batch.flux.FluxBatchLoadService;
import com.example.batch.service.NumberLoader;
import com.example.batch.service.OrderedNumber;
import com.example.batch.spliterator.SpliteratorBatchLoadService;
import com.example.batch.utils.TimeCounter;

import java.util.stream.Stream;

public class Main {

    private static final int BATCH_SIZE = 5;

    private static NumberLoader numberLoader = new NumberLoader(100_000_00);

    public static void main(String[] args) {
        try (TimeCounter counter = new TimeCounter()) {
            loadNumbersSpliterator(null)
                    .filter(num -> num.value() > 70)
                    .count();
        }
        try (TimeCounter counter = new TimeCounter()) {
            loadNumbersFlux(null)
                    .filter(num -> num.value() > 70)
                    .count();
        }
    }

    private static Stream<OrderedNumber> loadNumbersFlux(Integer startingAfter) {
        return FluxBatchLoadService.loadAll(
                sa -> numberLoader.getNumbers(sa, BATCH_SIZE).stream(),
                OrderedNumber::orderUnique,
                null
        );
    }

    private static Stream<OrderedNumber> loadNumbersSpliterator(Integer startingAfter) {
        return SpliteratorBatchLoadService.loadAll(
                sa -> numberLoader.getNumbers(sa, BATCH_SIZE).stream(),
                OrderedNumber::orderUnique,
                null
        );
    }

}
