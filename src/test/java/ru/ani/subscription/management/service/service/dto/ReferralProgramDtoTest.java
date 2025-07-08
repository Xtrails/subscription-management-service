package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ReferralProgramDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralProgramDto.class);
        ReferralProgramDto referralProgramDto1 = new ReferralProgramDto();
        referralProgramDto1.setId(UUID.randomUUID());
        ReferralProgramDto referralProgramDto2 = new ReferralProgramDto();
        assertThat(referralProgramDto1).isNotEqualTo(referralProgramDto2);
        referralProgramDto2.setId(referralProgramDto1.getId());
        assertThat(referralProgramDto1).isEqualTo(referralProgramDto2);
        referralProgramDto2.setId(UUID.randomUUID());
        assertThat(referralProgramDto1).isNotEqualTo(referralProgramDto2);
        referralProgramDto1.setId(null);
        assertThat(referralProgramDto1).isNotEqualTo(referralProgramDto2);
    }
}
