package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class ExternalUserDaoTestSamples {

    public static ExternalUserDao getExternalUserDaoSample1() {
        return new ExternalUserDao().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).externalUserId("externalUserId1");
    }

    public static ExternalUserDao getExternalUserDaoSample2() {
        return new ExternalUserDao().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).externalUserId("externalUserId2");
    }

    public static ExternalUserDao getExternalUserDaoRandomSampleGenerator() {
        return new ExternalUserDao().id(UUID.randomUUID()).externalUserId(UUID.randomUUID().toString());
    }
}
