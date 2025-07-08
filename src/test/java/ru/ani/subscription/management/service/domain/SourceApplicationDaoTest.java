package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SourceApplicationDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceApplicationDao.class);
        SourceApplicationDao sourceApplicationDao1 = getSourceApplicationDaoSample1();
        SourceApplicationDao sourceApplicationDao2 = new SourceApplicationDao();
        assertThat(sourceApplicationDao1).isNotEqualTo(sourceApplicationDao2);

        sourceApplicationDao2.setId(sourceApplicationDao1.getId());
        assertThat(sourceApplicationDao1).isEqualTo(sourceApplicationDao2);

        sourceApplicationDao2 = getSourceApplicationDaoSample2();
        assertThat(sourceApplicationDao1).isNotEqualTo(sourceApplicationDao2);
    }

    @Test
    void referralProgramsTest() {
        SourceApplicationDao sourceApplicationDao = getSourceApplicationDaoRandomSampleGenerator();
        ReferralProgramDao referralProgramDaoBack = getReferralProgramDaoRandomSampleGenerator();

        sourceApplicationDao.addReferralPrograms(referralProgramDaoBack);
        assertThat(sourceApplicationDao.getReferralPrograms()).containsOnly(referralProgramDaoBack);
        assertThat(referralProgramDaoBack.getSourceApplication()).isEqualTo(sourceApplicationDao);

        sourceApplicationDao.removeReferralPrograms(referralProgramDaoBack);
        assertThat(sourceApplicationDao.getReferralPrograms()).doesNotContain(referralProgramDaoBack);
        assertThat(referralProgramDaoBack.getSourceApplication()).isNull();

        sourceApplicationDao.referralPrograms(new HashSet<>(Set.of(referralProgramDaoBack)));
        assertThat(sourceApplicationDao.getReferralPrograms()).containsOnly(referralProgramDaoBack);
        assertThat(referralProgramDaoBack.getSourceApplication()).isEqualTo(sourceApplicationDao);

        sourceApplicationDao.setReferralPrograms(new HashSet<>());
        assertThat(sourceApplicationDao.getReferralPrograms()).doesNotContain(referralProgramDaoBack);
        assertThat(referralProgramDaoBack.getSourceApplication()).isNull();
    }

    @Test
    void subscriptionDetailsTest() {
        SourceApplicationDao sourceApplicationDao = getSourceApplicationDaoRandomSampleGenerator();
        SubscriptionDetailsDao subscriptionDetailsDaoBack = getSubscriptionDetailsDaoRandomSampleGenerator();

        sourceApplicationDao.addSubscriptionDetails(subscriptionDetailsDaoBack);
        assertThat(sourceApplicationDao.getSubscriptionDetails()).containsOnly(subscriptionDetailsDaoBack);
        assertThat(subscriptionDetailsDaoBack.getSourceApplication()).isEqualTo(sourceApplicationDao);

        sourceApplicationDao.removeSubscriptionDetails(subscriptionDetailsDaoBack);
        assertThat(sourceApplicationDao.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDaoBack);
        assertThat(subscriptionDetailsDaoBack.getSourceApplication()).isNull();

        sourceApplicationDao.subscriptionDetails(new HashSet<>(Set.of(subscriptionDetailsDaoBack)));
        assertThat(sourceApplicationDao.getSubscriptionDetails()).containsOnly(subscriptionDetailsDaoBack);
        assertThat(subscriptionDetailsDaoBack.getSourceApplication()).isEqualTo(sourceApplicationDao);

        sourceApplicationDao.setSubscriptionDetails(new HashSet<>());
        assertThat(sourceApplicationDao.getSubscriptionDetails()).doesNotContain(subscriptionDetailsDaoBack);
        assertThat(subscriptionDetailsDaoBack.getSourceApplication()).isNull();
    }

    @Test
    void userTest() {
        SourceApplicationDao sourceApplicationDao = getSourceApplicationDaoRandomSampleGenerator();
        ExternalUserDao externalUserDaoBack = getExternalUserDaoRandomSampleGenerator();

        sourceApplicationDao.setUser(externalUserDaoBack);
        assertThat(sourceApplicationDao.getUser()).isEqualTo(externalUserDaoBack);

        sourceApplicationDao.user(null);
        assertThat(sourceApplicationDao.getUser()).isNull();
    }
}
