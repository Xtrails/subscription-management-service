package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.SubscriptionDetailsAsserts.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionDetailsMapperTest {

    private SubscriptionDetailsMapper subscriptionDetailsMapper;

    @BeforeEach
    void setUp() {
        subscriptionDetailsMapper = new SubscriptionDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscriptionDetailsSample1();
        var actual = subscriptionDetailsMapper.toEntity(subscriptionDetailsMapper.toDto(expected));
        assertSubscriptionDetailsAllPropertiesEquals(expected, actual);
    }
}
