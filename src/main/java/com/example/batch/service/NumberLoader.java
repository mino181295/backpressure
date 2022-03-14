package com.example.batch.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class NumberLoader {

    private static final Random RANDOM = new Random();

    private final int size;
    private final List<OrderedNumber> numbers;

    public NumberLoader(int size) {
        this.size = size;
        this.numbers = IntStream.range(1, size + 1)
                .mapToObj(this::getOrderedNumber)
                .toList();
    }

    public List<OrderedNumber> getNumbers(Integer startingAfter, Integer limit) {
        // System.out.println("[NumberLoader] getNumbers {" + startingAfter + ", " + limit + "}");

        int sa = Optional.ofNullable(startingAfter).orElse(0);
        int l = Optional.ofNullable(limit).orElse(5);

        int min = Math.max(0, Math.min(sa, size));
        int max = Math.min(sa + l, size);
        return numbers.subList(min, max);
    }

    private OrderedNumber getOrderedNumber(int current) {
        int value = RANDOM.nextInt(100);
        return new OrderedNumber(current, value);
    }

}
