package ru.aniscan.subscription.management.service.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SourceApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SourceApplication getSourceApplicationSample1() {
        return new SourceApplication().id(1L).applicationName("applicationName1");
    }

    public static SourceApplication getSourceApplicationSample2() {
        return new SourceApplication().id(2L).applicationName("applicationName2");
    }

    public static SourceApplication getSourceApplicationRandomSampleGenerator() {
        return new SourceApplication().id(longCount.incrementAndGet()).applicationName(UUID.randomUUID().toString());
    }
}
