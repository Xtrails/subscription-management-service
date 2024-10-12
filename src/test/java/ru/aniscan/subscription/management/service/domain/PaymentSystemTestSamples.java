package ru.aniscan.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentSystemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentSystem getPaymentSystemSample1() {
        return new PaymentSystem().id(1L).name("name1");
    }

    public static PaymentSystem getPaymentSystemSample2() {
        return new PaymentSystem().id(2L).name("name2");
    }

    public static PaymentSystem getPaymentSystemRandomSampleGenerator() {
        return new PaymentSystem().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
