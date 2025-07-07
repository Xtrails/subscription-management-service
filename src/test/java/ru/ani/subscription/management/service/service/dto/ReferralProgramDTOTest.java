package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ReferralProgramDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralProgramDTO.class);
        ReferralProgramDTO referralProgramDTO1 = new ReferralProgramDTO();
        referralProgramDTO1.setId(1L);
        ReferralProgramDTO referralProgramDTO2 = new ReferralProgramDTO();
        assertThat(referralProgramDTO1).isNotEqualTo(referralProgramDTO2);
        referralProgramDTO2.setId(referralProgramDTO1.getId());
        assertThat(referralProgramDTO1).isEqualTo(referralProgramDTO2);
        referralProgramDTO2.setId(2L);
        assertThat(referralProgramDTO1).isNotEqualTo(referralProgramDTO2);
        referralProgramDTO1.setId(null);
        assertThat(referralProgramDTO1).isNotEqualTo(referralProgramDTO2);
    }
}
