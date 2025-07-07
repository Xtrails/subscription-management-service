package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.SourceApplicationAsserts.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationTestSamples.*;

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
        var expected = getSourceApplicationSample1();
        var actual = sourceApplicationMapper.toEntity(sourceApplicationMapper.toDto(expected));
        assertSourceApplicationAllPropertiesEquals(expected, actual);
    }
}
