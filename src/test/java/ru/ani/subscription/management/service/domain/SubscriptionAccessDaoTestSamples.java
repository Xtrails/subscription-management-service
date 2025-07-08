package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SubscriptionAccessDaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionAccessDao getSubscriptionAccessDaoSample1() {
        return new SubscriptionAccessDao()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .order(1)
            .role("role1")
            .roleGroup("roleGroup1");
    }

    public static SubscriptionAccessDao getSubscriptionAccessDaoSample2() {
        return new SubscriptionAccessDao()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .order(2)
            .role("role2")
            .roleGroup("roleGroup2");
    }

    public static SubscriptionAccessDao getSubscriptionAccessDaoRandomSampleGenerator() {
        return new SubscriptionAccessDao()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .order(intCount.incrementAndGet())
            .role(UUID.randomUUID().toString())
            .roleGroup(UUID.randomUUID().toString());
    }
}
