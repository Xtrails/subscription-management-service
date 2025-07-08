package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class SourceApplicationDaoTestSamples {

    public static SourceApplicationDao getSourceApplicationDaoSample1() {
        return new SourceApplicationDao().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).applicationName("applicationName1");
    }

    public static SourceApplicationDao getSourceApplicationDaoSample2() {
        return new SourceApplicationDao().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).applicationName("applicationName2");
    }

    public static SourceApplicationDao getSourceApplicationDaoRandomSampleGenerator() {
        return new SourceApplicationDao().id(UUID.randomUUID()).applicationName(UUID.randomUUID().toString());
    }
}
