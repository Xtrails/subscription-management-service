package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramTestSamples.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ReferralProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralProgram.class);
        ReferralProgram referralProgram1 = getReferralProgramSample1();
        ReferralProgram referralProgram2 = new ReferralProgram();
        assertThat(referralProgram1).isNotEqualTo(referralProgram2);

        referralProgram2.setId(referralProgram1.getId());
        assertThat(referralProgram1).isEqualTo(referralProgram2);

        referralProgram2 = getReferralProgramSample2();
        assertThat(referralProgram1).isNotEqualTo(referralProgram2);
    }

    @Test
    void externalUserTest() {
        ReferralProgram referralProgram = getReferralProgramRandomSampleGenerator();
        ExternalUser externalUserBack = getExternalUserRandomSampleGenerator();

        referralProgram.setExternalUser(externalUserBack);
        assertThat(referralProgram.getExternalUser()).isEqualTo(externalUserBack);
        assertThat(externalUserBack.getReferralCreator()).isEqualTo(referralProgram);

        referralProgram.externalUser(null);
        assertThat(referralProgram.getExternalUser()).isNull();
        assertThat(externalUserBack.getReferralCreator()).isNull();
    }

    @Test
    void sourceApplicationTest() {
        ReferralProgram referralProgram = getReferralProgramRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        referralProgram.setSourceApplication(sourceApplicationBack);
        assertThat(referralProgram.getSourceApplication()).isEqualTo(sourceApplicationBack);

        referralProgram.sourceApplication(null);
        assertThat(referralProgram.getSourceApplication()).isNull();
    }
}
