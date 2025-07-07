package ru.ani.subscription.management.service.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ClientSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClientSubscription getClientSubscriptionSample1() {
        return new ClientSubscription().id(1L);
    }

    public static ClientSubscription getClientSubscriptionSample2() {
        return new ClientSubscription().id(2L);
    }

    public static ClientSubscription getClientSubscriptionRandomSampleGenerator() {
        return new ClientSubscription().id(longCount.incrementAndGet());
    }
}
