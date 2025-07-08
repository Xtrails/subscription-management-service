package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.SubscriptionAccessDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.SubscriptionAccessDaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriptionAccessMapperTest {

    private SubscriptionAccessMapper subscriptionAccessMapper;

    @BeforeEach
    void setUp() {
        subscriptionAccessMapper = new SubscriptionAccessMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubscriptionAccessDaoSample1();
        var actual = subscriptionAccessMapper.toEntity(subscriptionAccessMapper.toDto(expected));
        assertSubscriptionAccessDaoAllPropertiesEquals(expected, actual);
    }
}
