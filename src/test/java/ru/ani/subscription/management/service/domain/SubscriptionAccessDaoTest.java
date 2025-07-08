package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.SubscriptionAccessDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionAccessDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionAccessDao.class);
        SubscriptionAccessDao subscriptionAccessDao1 = getSubscriptionAccessDaoSample1();
        SubscriptionAccessDao subscriptionAccessDao2 = new SubscriptionAccessDao();
        assertThat(subscriptionAccessDao1).isNotEqualTo(subscriptionAccessDao2);

        subscriptionAccessDao2.setId(subscriptionAccessDao1.getId());
        assertThat(subscriptionAccessDao1).isEqualTo(subscriptionAccessDao2);

        subscriptionAccessDao2 = getSubscriptionAccessDaoSample2();
        assertThat(subscriptionAccessDao1).isNotEqualTo(subscriptionAccessDao2);
    }

    @Test
    void subscriptionDetailsTest() {
        SubscriptionAccessDao subscriptionAccessDao = getSubscriptionAccessDaoRandomSampleGenerator();
        SubscriptionDetailsDao subscriptionDetailsDaoBack = getSubscriptionDetailsDaoRandomSampleGenerator();

        subscriptionAccessDao.addSubscriptionDetails(subscriptionDetailsDaoBack);
        assertThat(subscriptionAccessDao.getSubscriptionDetails()).containsOnly(subscriptionDetailsDaoBack);

        subscriptionAccessDao.removeSubscriptionDetails(subscriptionDetailsDaoBack);
        assertThat(subscriptionAccessDao.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDaoBack);

        subscriptionAccessDao.subscriptionDetails(new HashSet<>(Set.of(subscriptionDetailsDaoBack)));
        assertThat(subscriptionAccessDao.getSubscriptionDetails()).containsOnly(subscriptionDetailsDaoBack);

        subscriptionAccessDao.setSubscriptionDetails(new HashSet<>());
        assertThat(subscriptionAccessDao.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDaoBack);
    }
}
