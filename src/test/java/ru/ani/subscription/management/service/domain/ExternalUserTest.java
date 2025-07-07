package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ExternalUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalUser.class);
        ExternalUser externalUser1 = getExternalUserSample1();
        ExternalUser externalUser2 = new ExternalUser();
        assertThat(externalUser1).isNotEqualTo(externalUser2);

        externalUser2.setId(externalUser1.getId());
        assertThat(externalUser1).isEqualTo(externalUser2);

        externalUser2 = getExternalUserSample2();
        assertThat(externalUser1).isNotEqualTo(externalUser2);
    }

    @Test
    void referralCreatorTest() {
        ExternalUser externalUser = getExternalUserRandomSampleGenerator();
        ReferralProgram referralProgramBack = getReferralProgramRandomSampleGenerator();

        externalUser.setReferralCreator(referralProgramBack);
        assertThat(externalUser.getReferralCreator()).isEqualTo(referralProgramBack);

        externalUser.referralCreator(null);
        assertThat(externalUser.getReferralCreator()).isNull();
    }

    @Test
    void referralProgramTest() {
        ExternalUser externalUser = getExternalUserRandomSampleGenerator();
        ReferralProgram referralProgramBack = getReferralProgramRandomSampleGenerator();

        externalUser.setReferralProgram(referralProgramBack);
        assertThat(externalUser.getReferralProgram()).isEqualTo(referralProgramBack);

        externalUser.referralProgram(null);
        assertThat(externalUser.getReferralProgram()).isNull();
    }
}
