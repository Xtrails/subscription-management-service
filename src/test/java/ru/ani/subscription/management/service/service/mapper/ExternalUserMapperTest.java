package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.ExternalUserAsserts.*;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExternalUserMapperTest {

    private ExternalUserMapper externalUserMapper;

    @BeforeEach
    void setUp() {
        externalUserMapper = new ExternalUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExternalUserSample1();
        var actual = externalUserMapper.toEntity(externalUserMapper.toDto(expected));
        assertExternalUserAllPropertiesEquals(expected, actual);
    }
}
