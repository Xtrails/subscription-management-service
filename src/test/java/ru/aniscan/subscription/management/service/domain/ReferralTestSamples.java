package ru.aniscan.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReferralTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Referral getReferralSample1() {
        return new Referral().id(1L).referralCode("referralCode1");
    }

    public static Referral getReferralSample2() {
        return new Referral().id(2L).referralCode("referralCode2");
    }

    public static Referral getReferralRandomSampleGenerator() {
        return new Referral().id(longCount.incrementAndGet()).referralCode(UUID.randomUUID().toString());
    }
}
