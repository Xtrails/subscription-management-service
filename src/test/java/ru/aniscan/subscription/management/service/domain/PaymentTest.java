package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.ClientSubscriptionTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.PaymentSystemTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.PaymentTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void userTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        ExternalUser externalUserBack = getExternalUserRandomSampleGenerator();

        payment.setUser(externalUserBack);
        assertThat(payment.getUser()).isEqualTo(externalUserBack);

        payment.user(null);
        assertThat(payment.getUser()).isNull();
    }

    @Test
    void clientSubscriptionTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        ClientSubscription clientSubscriptionBack = getClientSubscriptionRandomSampleGenerator();

        payment.setClientSubscription(clientSubscriptionBack);
        assertThat(payment.getClientSubscription()).isEqualTo(clientSubscriptionBack);

        payment.clientSubscription(null);
        assertThat(payment.getClientSubscription()).isNull();
    }

    @Test
    void paymentSystemTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentSystem paymentSystemBack = getPaymentSystemRandomSampleGenerator();

        payment.setPaymentSystem(paymentSystemBack);
        assertThat(payment.getPaymentSystem()).isEqualTo(paymentSystemBack);

        payment.paymentSystem(null);
        assertThat(payment.getPaymentSystem()).isNull();
    }

    @Test
    void sourceApplicationTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        payment.setSourceApplication(sourceApplicationBack);
        assertThat(payment.getSourceApplication()).isEqualTo(sourceApplicationBack);

        payment.sourceApplication(null);
        assertThat(payment.getSourceApplication()).isNull();
    }
}
