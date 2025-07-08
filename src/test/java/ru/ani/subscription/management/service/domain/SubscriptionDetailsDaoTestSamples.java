package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SubscriptionDetailsDaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoSample1() {
        return new SubscriptionDetailsDao()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .duration(1);
    }

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoSample2() {
        return new SubscriptionDetailsDao()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .duration(2);
    }

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoRandomSampleGenerator() {
        return new SubscriptionDetailsDao()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet());
    }
}
