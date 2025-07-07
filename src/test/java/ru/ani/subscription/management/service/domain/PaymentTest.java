package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionTestSamples.*;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.ani.subscription.management.service.domain.PaymentSystemTestSamples.*;
import static ru.ani.subscription.management.service.domain.PaymentTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

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
    void clientSubscriptionTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        ClientSubscription clientSubscriptionBack = getClientSubscriptionRandomSampleGenerator();

        payment.setClientSubscription(clientSubscriptionBack);
        assertThat(payment.getClientSubscription()).isEqualTo(clientSubscriptionBack);

        payment.clientSubscription(null);
        assertThat(payment.getClientSubscription()).isNull();
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
    void clietntSubscriptionTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        ClientSubscription clientSubscriptionBack = getClientSubscriptionRandomSampleGenerator();

        payment.setClietntSubscription(clientSubscriptionBack);
        assertThat(payment.getClietntSubscription()).isEqualTo(clientSubscriptionBack);

        payment.clietntSubscription(null);
        assertThat(payment.getClietntSubscription()).isNull();
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
}
