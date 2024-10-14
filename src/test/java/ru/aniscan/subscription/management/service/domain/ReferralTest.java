package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.ReferralProgramTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.ReferralTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

class ReferralTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Referral.class);
        Referral referral1 = getReferralSample1();
        Referral referral2 = new Referral();
        assertThat(referral1).isNotEqualTo(referral2);

        referral2.setId(referral1.getId());
        assertThat(referral1).isEqualTo(referral2);

        referral2 = getReferralSample2();
        assertThat(referral1).isNotEqualTo(referral2);
    }

    @Test
    void referrerTest() {
        Referral referral = getReferralRandomSampleGenerator();
        ExternalUser externalUserBack = getExternalUserRandomSampleGenerator();

        referral.setReferrer(externalUserBack);
        assertThat(referral.getReferrer()).isEqualTo(externalUserBack);

        referral.referrer(null);
        assertThat(referral.getReferrer()).isNull();
    }

    @Test
    void referralProgramTest() {
        Referral referral = getReferralRandomSampleGenerator();
        ReferralProgram referralProgramBack = getReferralProgramRandomSampleGenerator();

        referral.setReferralProgram(referralProgramBack);
        assertThat(referral.getReferralProgram()).isEqualTo(referralProgramBack);

        referral.referralProgram(null);
        assertThat(referral.getReferralProgram()).isNull();
    }

    @Test
    void sourceApplicationTest() {
        Referral referral = getReferralRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        referral.setSourceApplication(sourceApplicationBack);
        assertThat(referral.getSourceApplication()).isEqualTo(sourceApplicationBack);

        referral.sourceApplication(null);
        assertThat(referral.getSourceApplication()).isNull();
    }
}
