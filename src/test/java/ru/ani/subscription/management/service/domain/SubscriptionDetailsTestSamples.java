package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionDetails getSubscriptionDetailsSample1() {
        return new SubscriptionDetails().id(1L).name("name1").description("description1").duration(1);
    }

    public static SubscriptionDetails getSubscriptionDetailsSample2() {
        return new SubscriptionDetails().id(2L).name("name2").description("description2").duration(2);
    }

    public static SubscriptionDetails getSubscriptionDetailsRandomSampleGenerator() {
        return new SubscriptionDetails()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet());
    }
}
