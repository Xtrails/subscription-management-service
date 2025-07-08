package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class ClientSubscriptionDaoTestSamples {

    public static ClientSubscriptionDao getClientSubscriptionDaoSample1() {
        return new ClientSubscriptionDao().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static ClientSubscriptionDao getClientSubscriptionDaoSample2() {
        return new ClientSubscriptionDao().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static ClientSubscriptionDao getClientSubscriptionDaoRandomSampleGenerator() {
        return new ClientSubscriptionDao().id(UUID.randomUUID());
    }
}
