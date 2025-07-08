package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.SourceApplicationDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationDaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceApplicationMapperTest {

    private SourceApplicationMapper sourceApplicationMapper;

    @BeforeEach
    void setUp() {
        sourceApplicationMapper = new SourceApplicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSourceApplicationDaoSample1();
        var actual = sourceApplicationMapper.toEntity(sourceApplicationMapper.toDto(expected));
        assertSourceApplicationDaoAllPropertiesEquals(expected, actual);
    }
}
