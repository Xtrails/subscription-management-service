package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.PaymentSystemTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.ReferralProgramTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SubscriptionTypeTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

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
    void subscriptionTypesTest() {
        SourceApplication sourceApplication = getSourceApplicationRandomSampleGenerator();
        SubscriptionType subscriptionTypeBack = getSubscriptionTypeRandomSampleGenerator();

        sourceApplication.addSubscriptionTypes(subscriptionTypeBack);
        assertThat(sourceApplication.getSubscriptionTypes()).containsOnly(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.removeSubscriptionTypes(subscriptionTypeBack);
        assertThat(sourceApplication.getSubscriptionTypes()).doesNotContain(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSourceApplication()).isNull();

        sourceApplication.subscriptionTypes(new HashSet<>(Set.of(subscriptionTypeBack)));
        assertThat(sourceApplication.getSubscriptionTypes()).containsOnly(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSourceApplication()).isEqualTo(sourceApplication);

        sourceApplication.setSubscriptionTypes(new HashSet<>());
        assertThat(sourceApplication.getSubscriptionTypes()).doesNotContain(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSourceApplication()).isNull();
    }

    @Test
    void paymentSystemsTest() {
        SourceApplication sourceApplication = getSourceApplicationRandomSampleGenerator();
        PaymentSystem paymentSystemBack = getPaymentSystemRandomSampleGenerator();

        sourceApplication.addPaymentSystems(paymentSystemBack);
        assertThat(sourceApplication.getPaymentSystems()).containsOnly(paymentSystemBack);
        assertThat(paymentSystemBack.getSourceApplications()).containsOnly(sourceApplication);

        sourceApplication.removePaymentSystems(paymentSystemBack);
        assertThat(sourceApplication.getPaymentSystems()).doesNotContain(paymentSystemBack);
        assertThat(paymentSystemBack.getSourceApplications()).doesNotContain(sourceApplication);

        sourceApplication.paymentSystems(new HashSet<>(Set.of(paymentSystemBack)));
        assertThat(sourceApplication.getPaymentSystems()).containsOnly(paymentSystemBack);
        assertThat(paymentSystemBack.getSourceApplications()).containsOnly(sourceApplication);

        sourceApplication.setPaymentSystems(new HashSet<>());
        assertThat(sourceApplication.getPaymentSystems()).doesNotContain(paymentSystemBack);
        assertThat(paymentSystemBack.getSourceApplications()).doesNotContain(sourceApplication);
    }
}
