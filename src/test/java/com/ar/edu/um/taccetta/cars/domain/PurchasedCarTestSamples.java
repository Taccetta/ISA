package com.ar.edu.um.taccetta.cars.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PurchasedCarTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchasedCar getPurchasedCarSample1() {
        return new PurchasedCar().id(1L);
    }

    public static PurchasedCar getPurchasedCarSample2() {
        return new PurchasedCar().id(2L);
    }

    public static PurchasedCar getPurchasedCarRandomSampleGenerator() {
        return new PurchasedCar().id(longCount.incrementAndGet());
    }
}
