package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.ReferralProgramAsserts.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReferralProgramMapperTest {

    private ReferralProgramMapper referralProgramMapper;

    @BeforeEach
    void setUp() {
        referralProgramMapper = new ReferralProgramMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReferralProgramSample1();
        var actual = referralProgramMapper.toEntity(referralProgramMapper.toDto(expected));
        assertReferralProgramAllPropertiesEquals(expected, actual);
    }
}
