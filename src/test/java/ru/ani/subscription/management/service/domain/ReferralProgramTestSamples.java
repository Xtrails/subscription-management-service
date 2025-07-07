package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReferralProgramTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReferralProgram getReferralProgramSample1() {
        return new ReferralProgram().id(1L).name("name1").referralCode("referralCode1").description("description1");
    }

    public static ReferralProgram getReferralProgramSample2() {
        return new ReferralProgram().id(2L).name("name2").referralCode("referralCode2").description("description2");
    }

    public static ReferralProgram getReferralProgramRandomSampleGenerator() {
        return new ReferralProgram()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .referralCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
