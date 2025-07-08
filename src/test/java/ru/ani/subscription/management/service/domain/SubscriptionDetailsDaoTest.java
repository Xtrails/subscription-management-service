package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.SourceApplicationDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionAccessDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionDetailsDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDetailsDao.class);
        SubscriptionDetailsDao subscriptionDetailsDao1 = getSubscriptionDetailsDaoSample1();
        SubscriptionDetailsDao subscriptionDetailsDao2 = new SubscriptionDetailsDao();
        assertThat(subscriptionDetailsDao1).isNotEqualTo(subscriptionDetailsDao2);

        subscriptionDetailsDao2.setId(subscriptionDetailsDao1.getId());
        assertThat(subscriptionDetailsDao1).isEqualTo(subscriptionDetailsDao2);

        subscriptionDetailsDao2 = getSubscriptionDetailsDaoSample2();
        assertThat(subscriptionDetailsDao1).isNotEqualTo(subscriptionDetailsDao2);
    }

    @Test
    void sourceApplicationTest() {
        SubscriptionDetailsDao subscriptionDetailsDao = getSubscriptionDetailsDaoRandomSampleGenerator();
        SourceApplicationDao sourceApplicationDaoBack = getSourceApplicationDaoRandomSampleGenerator();

        subscriptionDetailsDao.setSourceApplication(sourceApplicationDaoBack);
        assertThat(subscriptionDetailsDao.getSourceApplication()).isEqualTo(sourceApplicationDaoBack);

        subscriptionDetailsDao.sourceApplication(null);
        assertThat(subscriptionDetailsDao.getSourceApplication()).isNull();
    }

    @Test
    void subscriptionAccessTest() {
        SubscriptionDetailsDao subscriptionDetailsDao = getSubscriptionDetailsDaoRandomSampleGenerator();
        SubscriptionAccessDao subscriptionAccessDaoBack = getSubscriptionAccessDaoRandomSampleGenerator();

        subscriptionDetailsDao.addSubscriptionAccess(subscriptionAccessDaoBack);
        assertThat(subscriptionDetailsDao.getSubscriptionAccesses()).containsOnly(subscriptionAccessDaoBack);
        assertThat(subscriptionAccessDaoBack.getSubscriptionDetails()).containsOnly(subscriptionDetailsDao);

        subscriptionDetailsDao.removeSubscriptionAccess(subscriptionAccessDaoBack);
        assertThat(subscriptionDetailsDao.getSubscriptionAccesses()).doesNotContain(subscriptionAccessDaoBack);
        assertThat(subscriptionAccessDaoBack.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDao);

        subscriptionDetailsDao.subscriptionAccesses(new HashSet<>(Set.of(subscriptionAccessDaoBack)));
        assertThat(subscriptionDetailsDao.getSubscriptionAccesses()).containsOnly(subscriptionAccessDaoBack);
        assertThat(subscriptionAccessDaoBack.getSubscriptionDetails()).containsOnly(subscriptionDetailsDao);

        subscriptionDetailsDao.setSubscriptionAccesses(new HashSet<>());
        assertThat(subscriptionDetailsDao.getSubscriptionAccesses()).doesNotContain(subscriptionAccessDaoBack);
        assertThat(subscriptionAccessDaoBack.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDao);
    }
}
