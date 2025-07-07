package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExternalUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExternalUser getExternalUserSample1() {
        return new ExternalUser().id(1L).externalUserId("externalUserId1");
    }

    public static ExternalUser getExternalUserSample2() {
        return new ExternalUser().id(2L).externalUserId("externalUserId2");
    }

    public static ExternalUser getExternalUserRandomSampleGenerator() {
        return new ExternalUser().id(longCount.incrementAndGet()).externalUserId(UUID.randomUUID().toString());
    }
}
