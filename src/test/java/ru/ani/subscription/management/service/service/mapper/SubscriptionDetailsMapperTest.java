package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoTestSamples.*;

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
        var expected = getSubscriptionDetailsDaoSample1();
        var actual = subscriptionDetailsMapper.toEntity(subscriptionDetailsMapper.toDto(expected));
        assertSubscriptionDetailsDaoAllPropertiesEquals(expected, actual);
    }
}
