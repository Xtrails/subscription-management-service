package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramTestSamples.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SourceApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceApplication.class);
        SourceApplication sourceApplication1 = getSourceApplicationSample1();
        SourceApplication sourceApplication2 = new SourceApplication();
        assertThat(sourceApplication1).isNotEqualTo(sourceApplication2);

        sourceApplication2.setId(sourceApplication1.getId());
        assertThat(sourceApplication1).isEqualTo(sourceApplication2);

        sourceApplication2 = getSourceApplicationSample2();
        assertThat(sourceApplication1).isNotEqualTo(sourceApplication2);
    }

    @Test
    void referralProgramsTest() {
        SourceApplication sourceApplication = getSourceApplicationRandomSampleGenerator();
        ReferralProgram referralProgramBack = getReferralProgramRandomSampleGenerator();

        sourceApplication.addReferralPrograms(referralProgramBack);
        assertThat(sourceApplication.getReferralPrograms()).containsOnly(referralProgramBack);
        assertThat(referralProgramBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.removeReferralPrograms(referralProgramBack);
        assertThat(sourceApplication.getReferralPrograms()).doesNotContain(referralProgramBack);
        assertThat(referralProgramBack.getSourceApplication()).isNull();

        sourceApplication.referralPrograms(new HashSet<>(Set.of(referralProgramBack)));
        assertThat(sourceApplication.getReferralPrograms()).containsOnly(referralProgramBack);
        assertThat(referralProgramBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.setReferralPrograms(new HashSet<>());
        assertThat(sourceApplication.getReferralPrograms()).doesNotContain(referralProgramBack);
        assertThat(referralProgramBack.getSourceApplication()).isNull();
    }

    @Test
    void subscriptionDetailsTest() {
        SourceApplication sourceApplication = getSourceApplicationRandomSampleGenerator();
        SubscriptionDetails subscriptionDetailsBack = getSubscriptionDetailsRandomSampleGenerator();

        sourceApplication.addSubscriptionDetails(subscriptionDetailsBack);
        assertThat(sourceApplication.getSubscriptionDetails()).containsOnly(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.removeSubscriptionDetails(subscriptionDetailsBack);
        assertThat(sourceApplication.getSubscriptionDetails()).doesNotContain(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getSourceApplication()).isNull();

        sourceApplication.subscriptionDetails(new HashSet<>(Set.of(subscriptionDetailsBack)));
        assertThat(sourceApplication.getSubscriptionDetails()).containsOnly(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.setSubscriptionDetails(new HashSet<>());
        assertThat(sourceApplication.getSubscriptionDetails()).doesNotContain(subscriptionDetailsBack);
        assertThat(subscriptionDetailsBack.getSourceApplication()).isNull();
    }

    @Test
    void userTest() {
        SourceApplication sourceApplication = getSourceApplicationRandomSampleGenerator();
        ExternalUser externalUserBack = getExternalUserRandomSampleGenerator();

        sourceApplication.setUser(externalUserBack);
        assertThat(sourceApplication.getUser()).isEqualTo(externalUserBack);

        sourceApplication.user(null);
        assertThat(sourceApplication.getUser()).isNull();
    }
}
