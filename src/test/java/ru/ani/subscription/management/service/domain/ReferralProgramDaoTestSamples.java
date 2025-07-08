package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class ReferralProgramDaoTestSamples {

    public static ReferralProgramDao getReferralProgramDaoSample1() {
        return new ReferralProgramDao()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .referralCode("referralCode1")
            .description("description1");
    }

    public static ReferralProgramDao getReferralProgramDaoSample2() {
        return new ReferralProgramDao()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .referralCode("referralCode2")
            .description("description2");
    }

    public static ReferralProgramDao getReferralProgramDaoRandomSampleGenerator() {
        return new ReferralProgramDao()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .referralCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
