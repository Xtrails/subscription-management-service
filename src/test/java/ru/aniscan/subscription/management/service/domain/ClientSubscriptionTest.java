package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.ClientSubscriptionTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SubscriptionTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

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
    void subscriptionTypeTest() {
        ClientSubscription clientSubscription = getClientSubscriptionRandomSampleGenerator();
        SubscriptionType subscriptionTypeBack = getSubscriptionTypeRandomSampleGenerator();

        clientSubscription.setSubscriptionType(subscriptionTypeBack);
        assertThat(clientSubscription.getSubscriptionType()).isEqualTo(subscriptionTypeBack);

        clientSubscription.subscriptionType(null);
        assertThat(clientSubscription.getSubscriptionType()).isNull();
    }

    @Test
    void sourceApplicationTest() {
        ClientSubscription clientSubscription = getClientSubscriptionRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        clientSubscription.setSourceApplication(sourceApplicationBack);
        assertThat(clientSubscription.getSourceApplication()).isEqualTo(sourceApplicationBack);

        clientSubscription.sourceApplication(null);
        assertThat(clientSubscription.getSourceApplication()).isNull();
    }
}
