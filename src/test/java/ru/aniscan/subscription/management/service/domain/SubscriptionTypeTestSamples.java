package ru.aniscan.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubscriptionType getSubscriptionTypeSample1() {
        return new SubscriptionType().id(1L).name("name1").description("description1").duration("duration1");
    }

    public static SubscriptionType getSubscriptionTypeSample2() {
        return new SubscriptionType().id(2L).name("name2").description("description2").duration("duration2");
    }

    public static SubscriptionType getSubscriptionTypeRandomSampleGenerator() {
        return new SubscriptionType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString());
    }
}
