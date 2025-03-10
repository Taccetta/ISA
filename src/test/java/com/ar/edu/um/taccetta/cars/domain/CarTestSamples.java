package com.ar.edu.um.taccetta.cars.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Car getCarSample1() {
        return new Car().id(1L).model("model1").year("year1").available(1);
    }

    public static Car getCarSample2() {
        return new Car().id(2L).model("model2").year("year2").available(2);
    }

    public static Car getCarRandomSampleGenerator() {
        return new Car()
            .id(longCount.incrementAndGet())
            .model(UUID.randomUUID().toString())
            .year(UUID.randomUUID().toString())
            .available(intCount.incrementAndGet());
    }
}
