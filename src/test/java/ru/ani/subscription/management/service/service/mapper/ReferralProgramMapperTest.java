package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.ReferralProgramDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramDaoTestSamples.*;

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
        var expected = getReferralProgramDaoSample1();
        var actual = referralProgramMapper.toEntity(referralProgramMapper.toDto(expected));
        assertReferralProgramDaoAllPropertiesEquals(expected, actual);
    }
}
