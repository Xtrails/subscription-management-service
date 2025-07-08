package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.ClientSubscriptionDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionDaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientSubscriptionMapperTest {

    private ClientSubscriptionMapper clientSubscriptionMapper;

    @BeforeEach
    void setUp() {
        clientSubscriptionMapper = new ClientSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientSubscriptionDaoSample1();
        var actual = clientSubscriptionMapper.toEntity(clientSubscriptionMapper.toDto(expected));
        assertClientSubscriptionDaoAllPropertiesEquals(expected, actual);
    }
}
