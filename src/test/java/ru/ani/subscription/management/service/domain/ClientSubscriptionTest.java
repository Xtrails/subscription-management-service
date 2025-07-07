package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionTestSamples.*;
import static ru.ani.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.ani.subscription.management.service.domain.PaymentTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ClientSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientSubscription.class);
        ClientSubscription clientSubscription1 = getClientSubscriptionSample1();
        ClientSubscription clientSubscription2 = new ClientSubscription();
        assertThat(clientSubscription1).isNotEqualTo(clientSubscription2);

        clientSubscription2.setId(clientSubscription1.getId());
        assertThat(clientSubscription1).isEqualTo(clientSubscription2);

        clientSubscription2 = getClientSubscriptionSample2();
        assertThat(clientSubscription1).isNotEqualTo(clientSubscription2);
    }

    @Test
    void userTest() {
        ClientSubscription clientSubscription = getClientSubscriptionRandomSampleGenerator();
        ExternalUser externalUserBack = getExternalUserRandomSampleGenerator();

        clientSubscription.setUser(externalUserBack);
        assertThat(clientSubscription.getUser()).isEqualTo(externalUserBack);

        clientSubscription.user(null);
        assertThat(clientSubscription.getUser()).isNull();
    }

    @Test
    void subscriptionDetailsTest() {
        ClientSubscription clientSubscription = getClientSubscriptionRandomSampleGenerator();
        SubscriptionDetails subscriptionDetailsBack = getSubscriptionDetailsRandomSampleGenerator();

        clientSubscription.setSubscriptionDetails(subscriptionDetailsBack);
        assertThat(clientSubscription.getSubscriptionDetails()).isEqualTo(subscriptionDetailsBack);

        clientSubscription.subscriptionDetails(null);
        assertThat(clientSubscription.getSubscriptionDetails()).isNull();
    }

    @Test
    void paymentTest() {
        ClientSubscription clientSubscription = getClientSubscriptionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        clientSubscription.setPayment(paymentBack);
        assertThat(clientSubscription.getPayment()).isEqualTo(paymentBack);
        assertThat(paymentBack.getClientSubscription()).isEqualTo(clientSubscription);

        clientSubscription.payment(null);
        assertThat(clientSubscription.getPayment()).isNull();
        assertThat(paymentBack.getClientSubscription()).isNull();
    }
}
