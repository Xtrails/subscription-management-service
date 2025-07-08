package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class SubscriptionDetailsDaoTestSamples {

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoSample1() {
        return new SubscriptionDetailsDao()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .duration("duration1");
    }

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoSample2() {
        return new SubscriptionDetailsDao()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .duration("duration2");
    }

    public static SubscriptionDetailsDao getSubscriptionDetailsDaoRandomSampleGenerator() {
        return new SubscriptionDetailsDao()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString());
    }
}
