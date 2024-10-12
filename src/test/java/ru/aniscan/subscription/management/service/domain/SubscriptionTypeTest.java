package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SubscriptionTypeTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

class SubscriptionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionType.class);
        SubscriptionType subscriptionType1 = getSubscriptionTypeSample1();
        SubscriptionType subscriptionType2 = new SubscriptionType();
        assertThat(subscriptionType1).isNotEqualTo(subscriptionType2);

        subscriptionType2.setId(subscriptionType1.getId());
        assertThat(subscriptionType1).isEqualTo(subscriptionType2);

        subscriptionType2 = getSubscriptionTypeSample2();
        assertThat(subscriptionType1).isNotEqualTo(subscriptionType2);
    }

    @Test
    void sourceApplicationTest() {
        SubscriptionType subscriptionType = getSubscriptionTypeRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        subscriptionType.setSourceApplication(sourceApplicationBack);
        assertThat(subscriptionType.getSourceApplication()).isEqualTo(sourceApplicationBack);

        subscriptionType.sourceApplication(null);
        assertThat(subscriptionType.getSourceApplication()).isNull();
    }
}
