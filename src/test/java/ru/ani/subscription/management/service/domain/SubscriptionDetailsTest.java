package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.SourceApplicationTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDetails.class);
        SubscriptionDetails subscriptionDetails1 = getSubscriptionDetailsSample1();
        SubscriptionDetails subscriptionDetails2 = new SubscriptionDetails();
        assertThat(subscriptionDetails1).isNotEqualTo(subscriptionDetails2);

        subscriptionDetails2.setId(subscriptionDetails1.getId());
        assertThat(subscriptionDetails1).isEqualTo(subscriptionDetails2);

        subscriptionDetails2 = getSubscriptionDetailsSample2();
        assertThat(subscriptionDetails1).isNotEqualTo(subscriptionDetails2);
    }

    @Test
    void sourceApplicationTest() {
        SubscriptionDetails subscriptionDetails = getSubscriptionDetailsRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        subscriptionDetails.setSourceApplication(sourceApplicationBack);
        assertThat(subscriptionDetails.getSourceApplication()).isEqualTo(sourceApplicationBack);

        subscriptionDetails.sourceApplication(null);
        assertThat(subscriptionDetails.getSourceApplication()).isNull();
    }
}
